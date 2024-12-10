package repository;

import model.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static repository.Settings.*;

public class ActorRepository {

    private static final String GET_ACTOR_BY_ID_QUERY =
            "SELECT * FROM actor WHERE id = ?";

    private static final String ADD_ACTOR_QUERY =
            "INSERT INTO actor (firstName, lastName, dateOfBirth, awards) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_ACTOR_QUERY =
            "UPDATE actor SET firstName = ?, lastName = ?, dateOfBirth = ?, awards = ? WHERE id = ?";

    private static final String DELETE_ACTOR_QUERY =
            "DELETE FROM actor WHERE id = ?";

    public Optional<Actor> getActorById(Long id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_ACTOR_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getLong("id"));
                    actor.setFirstName(rs.getString("firstName"));
                    actor.setLastName(rs.getString("lastName"));
                    actor.setDateOfBirth(rs.getDate("dateOfBirth"));
                    return Optional.of(actor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void addActor(Actor actor) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(ADD_ACTOR_QUERY, Statement.RETURN_GENERATED_KEYS)) {

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
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(UPDATE_ACTOR_QUERY)) {

            stmt.setString(1, actor.getFirstName());
            stmt.setString(2, actor.getLastName());
            stmt.setDate(3, new Date(actor.getDateOfBirth().getTime()));
            stmt.setString(4, actor.getAwards().toString());
            stmt.setLong(5, actor.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteActor(Long actorId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(DELETE_ACTOR_QUERY)) {

            stmt.setLong(1, actorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
