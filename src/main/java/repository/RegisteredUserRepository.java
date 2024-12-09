package repository;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RegisteredUserRepository extends UserRepository {
    private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";
    public RegisteredUser getRegisteredUserById(Long id) {
        RegisteredUser user = null;
        User u = getUserById(id);
        if (u instanceof RegisteredUser) {
            user = (RegisteredUser)u;
            user.setLikedMovies(getLikedMoviesByUserId(user.getId()));
        }
        return user;
    }

    public List<Movie> getLikedMoviesByUserId(Long userId) {
        String query = "SELECT m.* FROM movie m JOIN user_watchlist uw ON m.id = uw.movie_id WHERE uw.user_id = ?";
        List<Movie> movies = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, userId);

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
