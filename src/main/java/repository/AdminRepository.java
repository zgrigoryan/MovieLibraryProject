package repository;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository extends UserRepository {
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        for (User user : getAllUsers()) {
            if (user instanceof Admin) {
                admins.add((Admin)user);
            }
        }
        return admins;
    }
}
