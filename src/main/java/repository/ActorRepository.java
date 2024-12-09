package repository;

import model.*;
import java.sql.*;
import java.util.*;

public class ActorRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";

    public Actor getActorById(Long id) {
        String query = "SELECT * FROM actor WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getLong("id"));
                    actor.setFirstName(rs.getString("firstName"));
                    actor.setLastName(rs.getString("lastName"));
                    actor.setDateOfBirth(rs.getDate("dateOfBirth"));
                    return actor;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addActor(Actor actor) {
        String query = "INSERT INTO actor (firstName, lastName, dateOfBirth, awards) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getLastName());
            stmt.setDate(3, new java.sql.Date(actor.getDateOfBirth().getTime()));
            stmt.setString(4, actor.getAwards().toString());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    actor.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateActor(Actor actor) {
        String query = "UPDATE actor SET firstName = ?, lastName = ?, dateOfBirth = ?, awards = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getLastName());
            stmt.setDate(3, new java.sql.Date(actor.getDateOfBirth().getTime()));
            stmt.setString(4, actor.getAwards().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(Long actorId) {
        String query = "DELETE FROM actor WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, actorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
