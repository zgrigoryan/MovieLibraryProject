package src.main.java.service;

import java.util.Map;
import src.main.java.model.*;

public class UserManagementService {
    private Map<Long, User> users;

    public UserManagementService(Map<Long, User> users) {
        this.users = users;
    }

    public void deleteUser(User user) {
        if (user == null) {
            System.out.println("Invalid user: User is null.");
            return;
        }

        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            System.out.println("User with ID " + user.getId() + " deleted successfully.");
        } else {
            System.out.println("User with ID " + user.getId() + " not found.");
        }
    }
}
