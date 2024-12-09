package repository;

import model.*;
import java.sql.*;
import java.util.*;

public class RecommendationEngineRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";

    public List<RegisteredUser> getAllRegisteredUsers() {
        String query = "SELECT * FROM \"user\"";
        List<RegisteredUser> users = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                RegisteredUser user = new RegisteredUser(id, name, email, password, new ArrayList<>());
                List<Movie> likedMovies = getLikedMoviesByUserId(user.getId());
                user.setLikedMovies(likedMovies);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Movie> getAllMovies() {
        String query = "SELECT * FROM movie";
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<Movie> getLikedMoviesByUserId(Long userId) {
        String query = "SELECT m.* FROM movie m " +
                "JOIN user_watchlist uw ON m.id = uw.movie_id " +
                "WHERE uw.user_id = ?";

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

    private List<Genre> getGenresByMovieId(Long movieId) {
        String query = "SELECT dg.* FROM def_genre dg " +
                "JOIN n2n_movie_to_genre mtg ON dg.id = mtg.genre_id " +
                "WHERE mtg.movie_id = ?";

        List<Genre> genres = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

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

    public Map<Long, User> getAllUsersMap() {
        List<RegisteredUser> userList = getAllRegisteredUsers();
        Map<Long, User> userMap = new HashMap<>();
        for (RegisteredUser u : userList) {
            userMap.put(u.getId(), u);
        }
        return userMap;
    }

    public Map<Long, Movie> getAllMoviesMap() {
        List<Movie> movieList = getAllMovies();
        Map<Long, Movie> movieMap = new HashMap<>();
        for (Movie m : movieList) {
            movieMap.put(m.getId(), m);
        }
        return movieMap;
    }

    private List<Actor> getActorsByMovieId(Long movieId) {
        String query = "SELECT a.* FROM actor a " +
                "JOIN n2n_movie_to_actor mta ON a.id = mta.actor_id " +
                "WHERE mta.movie_id = ?";

        List<Actor> actors = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

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
