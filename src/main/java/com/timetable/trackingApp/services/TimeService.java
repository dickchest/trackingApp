package com.timetable.trackingApp.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.timetable.trackingApp.domain.TimeEntries;
import com.timetable.trackingApp.dto.TimeEntriesDto;
import com.timetable.trackingApp.services.Utils.TimeConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeService {
    private final FirebaseAuthService firebaseAuthService;
    private final CategoryService categoryService;
    private final Firestore dbFirestore = FirestoreClient.getFirestore();
    private final CollectionReference collection = dbFirestore.collection("time_entries");

    public List<TimeEntriesDto> getAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = collection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream()
                .map(x -> x.toObject(TimeEntries.class))
                .map(TimeConverter::toDto)
                .toList();
    }

    public String create(TimeEntriesDto dto, Principal principal) {
        DocumentReference addedDocRef = collection.document();
        dto.setId(addedDocRef.getId());
        dto.setUserId(firebaseAuthService.getUserUid(principal));
        // проверить, есть ль категория
        categoryService.get(dto.getCategoryId());
//         проверить, есть ли начальные и конечные даты
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            Duration duration = Duration.between(dto.getStartDate(), dto.getEndDate());
            dto.setDuration(duration.getSeconds());
        }
        ApiFuture<WriteResult> writeResult = addedDocRef.set(TimeConverter.fromDto(dto));
        return addedDocRef.getId();
    }

    public TimeEntries get(String documentId) {
        DocumentSnapshot document = checkIfExistDocument(documentId);
        return document.toObject(TimeEntries.class);
    }

    public String update(TimeEntriesDto dto, Principal principal) throws ExecutionException, InterruptedException {
        // проверка, есть ли документ
        TimeEntriesDto request = TimeConverter.toDto(get(dto.getId()));
        // проверка, что редактируется свой отзыв
        if (!request.getUserId().equals(firebaseAuthService.getUserUid(principal))) {
            throw new RuntimeException("Not allowed!");
        }
        // проверяем каждое поле
        // проверить, есть ль категория
        if (dto.getCategoryId() != null) {
            if (categoryService.get(dto.getCategoryId()) != null) {
                request.setCategoryId(dto.getCategoryId());
            }
        }
        // проверяем меняется ли время
        if (dto.getStartDate() != null || dto.getEndDate() != null) {
            Optional.ofNullable(dto.getStartDate()).ifPresent(request::setStartDate);
            Optional.ofNullable(dto.getEndDate()).ifPresent(request::setEndDate);
            // вычисляем новый дюрейшн
            Duration duration = Duration.between(request.getStartDate(), request.getEndDate());
            request.setDuration(duration.getSeconds());
        }

        ApiFuture<WriteResult> collectionsApiFuture = collection.document(dto.getId()).set(TimeConverter.fromDto(request));
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String delete(String documentId) {
        // нужно проверить, есть ли документ
        DocumentSnapshot document = checkIfExistDocument(documentId);
        ApiFuture<WriteResult> collectionsApiFuture = collection.document(documentId).delete();
        return "Successfully deleted " + documentId;
    }

    private DocumentSnapshot checkIfExistDocument(String documentId) {
        DocumentReference documentReference = collection.document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document;
            } else {
                throw new RuntimeException("Entity Not Found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Map<String, List<TimeEntries>>> reportByWeeks() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = collection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<TimeEntries> entries = documents.stream()
                .map(doc -> doc.toObject(TimeEntries.class))
                .toList();

        // группируем записи по неделям года
        Map<Integer, List<TimeEntries>> entriesByWeek = entries.stream()
                .collect(Collectors.groupingBy(entry -> {
                    LocalDate startDate = entry.getStartDate().toDate().toInstant()
                            .atZone(ZoneId.systemDefault()).toLocalDate();
                    return startDate.get(WeekFields.of(Locale.getDefault()).weekOfYear());
                }));

        // группируем группы по категориях внутри недель
        Map<Integer, Map<String, List<TimeEntries>>> groupedByWeekAndCategory = new HashMap<>();
        for (Map.Entry<Integer, List<TimeEntries>> entry : entriesByWeek.entrySet()) {
            Map<String, List<TimeEntries>> groupedByCategory = entry.getValue().stream()
                    .collect(Collectors.groupingBy(TimeEntries::getCategoryId));
            groupedByWeekAndCategory.put(entry.getKey(), groupedByCategory);
        }

        return groupedByWeekAndCategory;
    }
}
