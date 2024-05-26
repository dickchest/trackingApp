package com.timetable.trackingApp.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.timetable.trackingApp.domain.Reviews;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class ReviewService {
    private final FirebaseAuthService firebaseAuthService;
    private final Firestore dbFirestore = FirestoreClient.getFirestore();
    private final CollectionReference collection = dbFirestore.collection("reviews");
    private final TopUserService topUserService;

    public ReviewService(FirebaseAuthService firebaseAuthService, ReviewService reviewService, TopUserService topUserService) {
        this.firebaseAuthService = firebaseAuthService;
        this.topUserService = topUserService;
    }

    public List<Reviews> getAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = collection.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.stream().map(x -> x.toObject(Reviews.class)).toList();
    }

    public String create(Reviews entity, Principal principal) {
        DocumentReference addedDocRef = collection.document();
        entity.setId(addedDocRef.getId());
        entity.setFromUserId(firebaseAuthService.getUserUid(principal));
        ApiFuture<WriteResult> writeResult = addedDocRef.set(entity);
        // todo реализовываем занесение в табилцу topUsers этого юзера
        if (entity.getRating() != null) {
            topUserService.create(entity.getToUserId(), entity.getRating());
        }
        return addedDocRef.getId();
    }

    public Reviews get(String documentId) {
        DocumentSnapshot document = checkIfExistDocument(documentId);
        return document.toObject(Reviews.class);
    }

    public String update(Reviews entity, Principal principal) throws ExecutionException, InterruptedException {
        // проверка, есть ли документ
        Reviews request = get(entity.getId());
        System.out.println(request.toString());
        // проверка, что редактируется свой отзыв
        if (!request.getFromUserId().equals(firebaseAuthService.getUserUid(principal))) {
            throw new RuntimeException("Not allowed!");
        }
        // проверяем каждое поле
        Optional.ofNullable(entity.getToUserId()).ifPresent(request::setToUserId);
        // todo если рейтинг меняется - реализовываем занесение в табилцу topUsers этого юзера
        Optional.ofNullable(entity.getRating()).ifPresent(request::setRating);
        Optional.ofNullable(entity.getComment()).ifPresent(request::setComment);

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
