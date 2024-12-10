package repository;

import model.*;

import java.sql.*;
import java.util.*;
import static repository.Settings.*;

public class UserRepository {

    private static final String GET_USER_BY_ID_QUERY =
            "SELECT * FROM \"user\" WHERE id = ?";

    private static final String ADD_USER_QUERY =
            "INSERT INTO \"user\" (name, email, password) VALUES (?, ?, ?)";

    private static final String UPDATE_USER_QUERY =
            "UPDATE \"user\" SET name = ?, email = ?, password = ? WHERE id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM \"user\" WHERE id = ?";

    private static final String GET_ALL_USERS_QUERY =
            "SELECT * FROM \"user\"";

    private static final String IS_EMAIL_REGISTERED_QUERY =
            "SELECT 1 FROM \"user\" WHERE LOWER(email) = LOWER(?)";

    public User getUserById(Long id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_USER_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return constructUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(User user, String userType) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(ADD_USER_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        user.setId(keys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_QUERY)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setLong(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(Long userId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER_QUERY)) {

            stmt.setLong(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_USERS_QUERY)) {

            while (rs.next()) {
                users.add(constructUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private User constructUserFromResultSet(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String userType = rs.getString("user_type");

        switch (userType.toUpperCase()) {
            case "ADMIN":
                return new Admin(id, name, email, password, "ADMIN_ROLE");
            case "GUEST":
                return new Guest(id, name, email, password);
            case "REGISTERED":
                List<Movie> likedMovies = new ArrayList<>();
                return new RegisteredUser(id, name, email, password, new ArrayList<>());
            default:
                return new Guest(id, name, email, password);
        }
    }

    public boolean isEmailRegistered(String email) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(IS_EMAIL_REGISTERED_QUERY)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
