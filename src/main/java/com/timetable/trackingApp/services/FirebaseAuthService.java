package com.timetable.trackingApp.services;
import com.google.firebase.auth.*;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FirebaseAuthService {
    public String getUserUid(Principal principal) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(principal.getName());
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
        return principal.getName();
    }

    public String getUserEmail(Principal principal){
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUser(principal.getName());
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }
        return userRecord.getEmail();
    }

    public List<String> getAll(){
        List<ExportedUserRecord> allUsers = getAllUserRecords();
        return allUsers.stream().map(UserRecord::getUid).toList();
    }



    private List<ExportedUserRecord> getAllUserRecords () {
        List<ExportedUserRecord> users = new ArrayList<>();
        ListUsersPage page;

        try {
            page = FirebaseAuth.getInstance().listUsers(null);
            page.iterateAll().forEach(users::add);
        } catch (FirebaseAuthException e) {
            throw new RuntimeException(e);
        }

        return users;
    }
}
