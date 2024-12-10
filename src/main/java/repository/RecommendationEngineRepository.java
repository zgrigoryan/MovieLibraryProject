package repository;

import model.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static repository.Settings.*;

public class RecommendationEngineRepository {

    private static final String GET_ALL_REGISTERED_USERS_QUERY = "SELECT * FROM \"user\"";

    private static final String GET_ALL_MOVIES_QUERY = "SELECT * FROM movie";

    private static final String GET_LIKED_MOVIES_BY_USER_ID_QUERY =
            "SELECT m.* FROM movie m " +
                    "JOIN user_watchlist uw ON m.id = uw.movie_id " +
                    "WHERE uw.user_id = ?";

    private static final String GET_GENRES_BY_MOVIE_ID_QUERY =
            "SELECT dg.* FROM def_genre dg " +
                    "JOIN n2n_movie_to_genre mtg ON dg.id = mtg.genre_id " +
                    "WHERE mtg.movie_id = ?";

    private static final String GET_ACTORS_BY_MOVIE_ID_QUERY =
            "SELECT a.* FROM actor a " +
                    "JOIN n2n_movie_to_actor mta ON a.id = mta.actor_id " +
                    "WHERE mta.movie_id = ?";

    public Map<Long, RegisteredUser> getAllRegisteredUsers() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_REGISTERED_USERS_QUERY)) {

            return extractUsersFromResultSet(rs)
                    .stream()
                    .collect(Collectors.toMap(
                            RegisteredUser::getId,
                            user -> user,
                            (existing, replacement) -> existing
                    ));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    private List<RegisteredUser> extractUsersFromResultSet(ResultSet rs) throws SQLException {
        List<RegisteredUser> users = new ArrayList<>();
        while (rs.next()) {
            try {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                RegisteredUser user = new RegisteredUser(id, name, email, password, new ArrayList<>());
                List<Movie> likedMovies = getLikedMoviesByUserId(user.getId());
                user.setLikedMovies(likedMovies);
                users.add(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;
    }


    public Map<Long, Movie> getAllMoviesMap() {
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_MOVIES_QUERY)) {

            while (rs.next()) {
                try {
                    Movie movie = new Movie();
                    movie.setId(rs.getLong("id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setReleaseDate(rs.getDate("releaseDate"));
                    movie.setDirector(rs.getString("director"));
                    movie.setAverageRating(rs.getDouble("average_rating"));

                    List<Genre> genres = getGenresByMovieId(movie.getId());
                    movie.setGenre(genres);

                    List<Actor> cast = getActorsByMovieId(movie.getId());
                    movie.setCast(cast);

                    movies.add(movie);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies.stream()
                .collect(Collectors.toMap(
                        Movie::getId,
                        movie -> movie,
                        (existing, replacement) -> existing
                ));
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

                    List<Genre> genres = getGenresByMovieId(movie.getId());
                    movie.setGenre(genres);

                    List<Actor> cast = getActorsByMovieId(movie.getId());
                    movie.setCast(cast);

                    movies.add(movie);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public Map<Long, User> getAllUsersMap() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_REGISTERED_USERS_QUERY)) {

            return extractUsersFromResultSet(rs)
                    .stream()
                    .collect(Collectors.toMap(
                            RegisteredUser::getId,
                            user -> user,
                            (existing, replacement) -> existing
                    ));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }


    private List<Genre> getGenresByMovieId(Long movieId) {
        List<Genre> genres = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_GENRES_BY_MOVIE_ID_QUERY)) {

            stmt.setLong(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Genre genre = new Genre();
                    genre.setId(rs.getLong("id"));
                    genre.setName(rs.getString("name"));
                    genres.add(genre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    private List<Actor> getActorsByMovieId(Long movieId) {
        List<Actor> actors = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_ACTORS_BY_MOVIE_ID_QUERY)) {

            stmt.setLong(1, movieId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getLong("id"));
                    actor.setFirstName(rs.getString("firstName"));
                    actor.setLastName(rs.getString("lastName"));
                    actor.setDateOfBirth(rs.getDate("dateOfBirth"));
                    actors.add(actor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actors;
    }
}
