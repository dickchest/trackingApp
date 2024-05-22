package com.timetable.trackingApp.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseAuthService {
    public FirebaseToken verifyIdToken(String idToken) {

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            return decodedToken;
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        }
    }
}
