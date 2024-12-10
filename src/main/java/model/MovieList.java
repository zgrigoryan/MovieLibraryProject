package model;

import java.util.*;
import service.*;
public class MovieList {
    private Long id;
    private String name;
    private String description;
    private List<Movie> movies;

    public MovieList(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.movies = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }

    @Override
    public String toString() {
        return "List Name: " + name + "\nDescription: " + description + "\nMovies: " + movies;
    }
}
