package service;

import model.*;
import repository.UserRepository;
import java.util.List;

public class RegistrationService {
    private UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Guest registerGuest(String name, String email, String password) {
        if (email == null || email.isEmpty() || userRepository.isEmailRegistered(email)) {
            throw new IllegalArgumentException("Invalid or already registered email");
        }

        Guest newGuest = new Guest(null, name, email, password);
        userRepository.addUser(newGuest, "GUEST");
        return newGuest;
    }
}
