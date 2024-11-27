import java.util.*;
import java.util.stream.Collectors;

public class MovieManagementService {
    private Map<Long, Movie> movies = new HashMap<>();
    private Long movieCounter = 1L;

    public void addMovie(String title, List<Genre> genre, String director, Date releaseDate, List<Actor> cast) {
        Long movieID = generateNextMovieId();
        Movie newMovie = new Movie(movieID, title, genre, director, releaseDate, cast);
        movies.put(movieID, newMovie);
    }

    public void updateMovie(Long movieId, Movie updatedMovie) {
        if (movies.containsKey(movieId)) {
            Movie existingMovie = movies.get(movieId);

            if (updatedMovie.getTitle() != null) {
                existingMovie.setTitle(updatedMovie.getTitle());
            }
            if (updatedMovie.getDirector() != null) {
                existingMovie.setDirector(updatedMovie.getDirector());
            }
            if (updatedMovie.getReleaseDate() != null) {
                existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
            }
            if (updatedMovie.getGenre() != null) {
                existingMovie.setGenre(updatedMovie.getGenre());
            }
            if (updatedMovie.getCast() != null) {
                existingMovie.setCast(updatedMovie.getCast());
            }
            if (updatedMovie.getAverageRating() != null) {
                existingMovie.setAverageRating(updatedMovie.getAverageRating());
            }

            movies.put(movieId, existingMovie);
        } else {
            System.out.println("Movie with ID " + movieId + " not found.");
        }
    }


    public void deleteMovie(Long id) {
        movies.remove(id);
    }

    public List<Movie> searchMovies(String keyword, Genre genre, String director) {
        return movies.values().stream()
                .filter(movie -> (keyword == null || movie.getTitle().toLowerCase().contains(keyword.toLowerCase())) &&
                        (genre == null || movie.getGenre().contains(genre)) &&
                        (director == null || movie.getDirector().equalsIgnoreCase(director)))
                .collect(Collectors.toList());
    }

    private Long generateNextMovieId() {
        return movieCounter++;
    }
}
