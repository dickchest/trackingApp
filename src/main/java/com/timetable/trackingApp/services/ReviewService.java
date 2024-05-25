package com.timetable.trackingApp.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;
import com.timetable.trackingApp.domain.Reviews;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class ReviewService {
    FirebaseAuthService firebaseAuthService;

    public List<Reviews> getAll() throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = dbFirestore.collection("reviews").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.stream().map(x -> x.toObject(Reviews.class)).toList();
    }

    public String create(Reviews entity, Principal principal) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference addedDocRef = dbFirestore.collection("reviews").document();
        entity.setId(addedDocRef.getId());
        entity.setFromUserId(firebaseAuthService.getUserUid(principal));
        UserRecord toUserRecord;
        ApiFuture<WriteResult> writeResult = addedDocRef.set(entity);
        return addedDocRef.getId();
    }


    public Reviews get(String documentId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("reviews").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            DocumentSnapshot document = future.get();
            return document.toObject(Reviews.class);
        } catch (Exception e) {
            throw new RuntimeException("Not Found");
        }
    }

    public String update(Reviews entity) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // находим документ в бд
        Reviews request = get(entity.getId());
        // проврка каждого поля
        Optional.ofNullable(entity.getToUserId()).ifPresent(request::setToUserId);
        Optional.ofNullable(entity.getRating()).ifPresent(request::setRating);
        Optional.ofNullable(entity.getComment()).ifPresent(request::setComment);

        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("reviews").document(entity.getId()).set(request);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String delete(String documentId) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("reviews").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            DocumentSnapshot document = future.get();
            ApiFuture<WriteResult> collectionsApiFuture = documentReference.delete();
            return "Successfully deleted";
        } catch (Exception e) {
            throw new RuntimeException("Not Found");
        }
    }
}
