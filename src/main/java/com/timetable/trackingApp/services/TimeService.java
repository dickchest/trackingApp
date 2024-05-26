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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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

    public String update(TimeEntries entity, Principal principal) throws ExecutionException, InterruptedException {
        // проверка, есть ли документ
        TimeEntries request = get(entity.getId());
        // проверка, что редактируется свой отзыв
        if (!request.getUserId().equals(firebaseAuthService.getUserUid(principal))) {
            throw new RuntimeException("Not allowed!");
        }
        // проверяем каждое поле
        // проверить, есть ль категория
        if (!entity.getCategoryId().isEmpty()) {
            if (categoryService.get(entity.getCategoryId()) != null) {
                request.setCategoryId(entity.getCategoryId());
            }
        }
        // проверяем меняется ли время
        if (entity.getStartDate() != null || entity.getEndDate() != null) {
            Optional.ofNullable(entity.getStartDate()).ifPresent(request::setStartDate);
            Optional.ofNullable(entity.getEndDate()).ifPresent(request::setEndDate);
            // вычисляем новый дюрейшн
//            request.setDuration(Duration.between(entity.getStartDate(), entity.getEndDate()).getSeconds());
        }

        ApiFuture<WriteResult> collectionsApiFuture = collection.document(entity.getId()).set(request);
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
}
