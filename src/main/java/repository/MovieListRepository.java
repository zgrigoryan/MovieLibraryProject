package repository;

import model.*;
import java.sql.*;
import java.util.*;

import static repository.Settings.*;

public class MovieListRepository {

    private static final String ADD_MOVIE_TO_LIST_QUERY =
            "INSERT INTO n2n_list_to_movie (list_id, movie_id) VALUES (?, ?)";

    private static final String CREATE_MOVIE_LIST_QUERY =
            "INSERT INTO movielist (user_id, name, description) VALUES (?, ?, ?)";

    private static final String GET_MOVIE_LIST_BY_NAME_QUERY =
            "SELECT * FROM movielist WHERE user_id = ? AND name = ?";

    private static final String GET_MOVIES_FOR_LIST_QUERY =
            "SELECT m.* FROM movie m JOIN n2n_list_to_movie lm ON m.id = lm.movie_id WHERE lm.list_id = ?";

    public void createMovieList(Long userId, String name, String description) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(CREATE_MOVIE_LIST_QUERY)) {

            stmt.setLong(1, userId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<MovieList> getMovieListByName(Long userId, String listName) {
        MovieList movieList;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_MOVIE_LIST_BY_NAME_QUERY)) {

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
                    return Optional.of(movieList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void addMovieToList(Long listId, Long movieId) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(ADD_MOVIE_TO_LIST_QUERY)) {

            stmt.setLong(1, listId);
            stmt.setLong(2, movieId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Movie> getMoviesForList(Long listId) {
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GET_MOVIES_FOR_LIST_QUERY)) {

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
