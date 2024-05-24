package com.timetable.trackingApp.services;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class FirebaseAuthService {
    public String getUserName(Principal principal) throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(principal.getName());
        return principal.getName();
    }
}
