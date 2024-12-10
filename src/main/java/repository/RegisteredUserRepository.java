package repository;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static repository.Settings.*;

public class RegisteredUserRepository extends UserRepository {

    private static final String GET_LIKED_MOVIES_BY_USER_ID_QUERY =
            "SELECT m.* FROM movie m " +
                    "JOIN user_watchlist uw ON m.id = uw.movie_id " +
                    "WHERE uw.user_id = ?";

    public RegisteredUser getRegisteredUserById(Long id) {
        RegisteredUser user = null;
        User u = getUserById(id);
        if (u instanceof RegisteredUser) {
            user = (RegisteredUser) u;
            user.setLikedMovies(getLikedMoviesByUserId(user.getId()));
        }
        return user;
    }

    public List<Movie> getLikedMoviesByUserId(Long userId) {
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_LIKED_MOVIES_BY_USER_ID_QUERY)) {

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
