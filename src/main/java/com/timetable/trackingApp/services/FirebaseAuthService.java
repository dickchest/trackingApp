package com.timetable.trackingApp.services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class FirebaseAuthService {
    public String getUserName(Principal principal) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(principal.getName());
        } catch (FirebaseAuthException e) {
            return "It's impossible to retrieve a user data";
        }
        return principal.getName();
    }

    public String getUserEmail(Principal principal){
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUser(principal.getName());
        } catch (FirebaseAuthException e) {
            return "It's impossible to retrieve a user data";
        }
        return userRecord.getEmail();
    }
}
