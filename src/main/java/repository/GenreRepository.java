package repository;

import model.*;
import java.sql.*;
import java.util.*;

import static repository.Settings.*;

public class GenreRepository {

    private static final String GET_GENRE_BY_ID_QUERY =
            "SELECT * FROM def_genre WHERE id = ?";

    private static final String ADD_GENRE_QUERY =
            "INSERT INTO def_genre (name) VALUES (?)";

    private static final String UPDATE_GENRE_QUERY =
            "UPDATE def_genre SET name = ? WHERE id = ?";

    private static final String DELETE_GENRE_QUERY =
            "DELETE FROM def_genre WHERE id = ?";

    public Optional<Genre> getGenreById(Long id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_GENRE_BY_ID_QUERY)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Genre genre = new Genre();
                    genre.setId(rs.getLong("id"));
                    genre.setName(rs.getString("name"));
                    return Optional.of(genre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void addGenre(Genre genre) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(ADD_GENRE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, genre.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    genre.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGenre(Genre genre) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(UPDATE_GENRE_QUERY)) {

            stmt.setString(1, genre.getName());
            stmt.setLong(2, genre.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteGenre(Long genreId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(DELETE_GENRE_QUERY)) {

            stmt.setLong(1, genreId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
