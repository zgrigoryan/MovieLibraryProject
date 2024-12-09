package service;

import model.User;
import repository.UserRepository;

public class UserManagementService {
    private UserRepository userRepository;

    public UserManagementService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteUser(User user) {
        if (user == null) {
            System.out.println("Invalid user: User is null.");
            return;
        }

        User existingUser = userRepository.getUserById(user.getId());
        if (existingUser != null) {
            userRepository.deleteUser(user.getId());
            System.out.println("User with ID " + user.getId() + " deleted successfully.");
        } else {
            System.out.println("User with ID " + user.getId() + " not found.");
        }
    }
}
