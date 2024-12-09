package service;

import model.*;
import repository.*;
import java.util.*;

public class MovieManagementService {
    private MovieRepository movieRepository;

    public MovieManagementService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void addMovie(String title, List<Genre> genre, String director, Date releaseDate, List<Actor> cast) {
        Movie newMovie = new Movie();
        newMovie.setTitle(title);
        newMovie.setDirector(director);
        newMovie.setReleaseDate(releaseDate);
        newMovie.setAverageRating(0.0);
        newMovie.setGenre(genre);
        newMovie.setCast(cast);

        movieRepository.addMovie(newMovie);
    }

    public void updateMovie(Long movieId, Movie updatedMovie) {
        updatedMovie.setId(movieId);
        movieRepository.updateMovie(updatedMovie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteMovie(id);
    }

    public List<Movie> searchMovies(String keyword, Genre genre, String director) {
        return movieRepository.searchMovies(keyword, genre, director);
    }
}
