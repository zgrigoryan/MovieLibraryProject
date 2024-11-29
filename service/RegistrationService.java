package service;

import java.util.*;
import model.*;

public class RegistrationService {
    private Map<Long, User> users = new HashMap<>();
    private Long userCounter = 1L;

    public Guest registerGuest(String name, String email, String password) {
        if (email == null || email.isEmpty() || isEmailRegistered(email)) {
            throw new IllegalArgumentException("Invalid or already registered email");
        }

        Long userId = generateNextUserId();
        Guest newGuest = new Guest(userId, name, email, password);
        users.put(userId, newGuest);

        return newGuest;
    }

    private boolean isEmailRegistered(String email) {
        return users.values().stream().anyMatch(user -> user.getEmail().equalsIgnoreCase(email));
    }

    private Long generateNextUserId() {
        return userCounter++;
    }
}
