package com.timetable.trackingApp.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.timetable.trackingApp.domain.TopUsers;
import com.timetable.trackingApp.validation.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class TopUserService {
    Firestore dbFirestore = FirestoreClient.getFirestore();
    CollectionReference collection = dbFirestore.collection("top_users");

    public void deleteUsersExceptTopTen() {
        // получаем документы, остортированные по полю rating в порядке убывания
        ApiFuture<QuerySnapshot> query = collection.orderBy("rating", Query.Direction.DESCENDING).get();

        try {
            // получаем список всех юзеров
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();

            // пропускаем первые 10 юзеров
            List<QueryDocumentSnapshot> usersToDelete = documents.stream().skip(10).toList();

            // Удаляем оставшихся пользователей
            for (QueryDocumentSnapshot doc : usersToDelete) {
                collection.document(doc.getId()).delete();
            }
            System.out.println("All users except top 10 have been deleted");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TopUsers> getAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = collection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.stream().map(x -> x.toObject(TopUsers.class)).toList();
    }

    public void add(String userId, Integer rating) {
        TopUsers entity;
        try {
            // юзер уже существует в таблице
            entity = get(userId);
            System.out.println("найдена запись " + entity.getId());
            System.out.println(entity.getUserId());
        } catch (NotFoundException e) {
            // юзер нет в таблице
            DocumentReference addedDocRef = collection.document();
            entity = new TopUsers();
            entity.setId(addedDocRef.getId());
            entity.setUserId(userId);
        }
        entity.setRating(rating);
        ApiFuture<WriteResult> collectionsApiFuture = collection.document(entity.getId()).set(entity);
    }

    public TopUsers get(String userId) {
        Optional<DocumentSnapshot> document = findDocumentByUserId(userId);
        return document.map(doc -> doc.toObject(TopUsers.class))
                .orElseThrow(() -> new NotFoundException("Not found"));
    }

    private Optional<DocumentSnapshot> findDocumentByUserId(String userId) {
        // Запрос к коллекции для поиска документов, где поле userId соответствует переданному значению
        ApiFuture<QuerySnapshot> future = collection.whereEqualTo("userId", userId).get();

        // Получаем результат выполнения запроса
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (documents.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(documents.get(0));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Can't access to top_user");
        }
    }
}
