package repository;

import model.*;
import java.sql.*;
import java.util.*;

import static repository.Settings.*;

public class MovieRepository {

    private static final String ADD_MOVIE_QUERY =
            "INSERT INTO movie (title, releaseDate, director, average_rating) VALUES (?, ?, ?, ?)";

    private static final String UPDATE_MOVIE_QUERY =
            "UPDATE movie SET title = ?, releaseDate = ?, director = ?, average_rating = ? WHERE id = ?";

    private static final String DELETE_MOVIE_QUERY =
            "DELETE FROM movie WHERE id = ?";

    private static final String SEARCH_MOVIES_QUERY =
            "SELECT * FROM movie WHERE " +
                    "(? IS NULL OR title ILIKE ?) " +
                    "AND (? IS NULL OR EXISTS (SELECT 1 FROM n2n_movie_to_genre WHERE movie_id = movie.id AND genre_id = ?)) " +
                    "AND (? IS NULL OR director = ?)";

    public void addMovie(Movie movie) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(ADD_MOVIE_QUERY, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, movie.getTitle());
            stmt.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
            stmt.setString(3, movie.getDirector());
            stmt.setDouble(4, movie.getAverageRating());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long movieId = rs.getLong(1);
                        movie.setId(movieId);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMovie(Movie movie) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(UPDATE_MOVIE_QUERY)) {

            stmt.setString(1, movie.getTitle());
            stmt.setDate(2, new java.sql.Date(movie.getReleaseDate().getTime()));
            stmt.setString(3, movie.getDirector());
            stmt.setDouble(4, movie.getAverageRating());
            stmt.setLong(5, movie.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMovie(Long movieId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(DELETE_MOVIE_QUERY)) {

            stmt.setLong(1, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> searchMovies(String keyword, Genre genre, String director) {
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(SEARCH_MOVIES_QUERY)) {

            stmt.setString(1, keyword);
            stmt.setString(2, "%" + keyword + "%");
            stmt.setObject(3, genre != null ? genre.getId() : null);
            stmt.setObject(4, genre != null ? genre.getId() : null);
            stmt.setString(5, director);
            stmt.setString(6, director);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movie movie = new Movie();
                    movie.setId(rs.getLong("id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setReleaseDate(rs.getDate("releaseDate"));
                    movie.setDirector(rs.getString("director"));
                    movie.setAverageRating(rs.getDouble("average_rating"));

                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }
}
