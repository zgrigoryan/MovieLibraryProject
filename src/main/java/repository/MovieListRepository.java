package repository;

import model.*;
import java.sql.*;
import java.util.*;

public class MovieListRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";

    public void createMovieList(Long userId, String name, String description) {
        String query = "INSERT INTO movielist (user_id, name, description) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MovieList getMovieListByName(Long userId, String listName) {
        String query = "SELECT * FROM movielist WHERE user_id = ? AND name = ?";
        MovieList movieList = null;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            stmt.setString(2, listName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long listId = rs.getLong("id");
                    movieList = new MovieList(listId, rs.getString("name"), rs.getString("description"));
                    List<Movie> movies = getMoviesForList(listId);
                    for (Movie m : movies) {
                        movieList.addMovie(m);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieList;
    }


    public void addMovieToList(Long listId, Long movieId) {
        String query = "INSERT INTO n2n_list_to_movie (list_id, movie_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, listId);
            stmt.setLong(2, movieId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Movie> getMoviesForList(Long listId) {
        String query = "SELECT m.* FROM movie m JOIN n2n_list_to_movie lm ON m.id = lm.movie_id WHERE lm.list_id = ?";
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, listId);
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
