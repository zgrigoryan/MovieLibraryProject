package service;

import model.*;
import repository.*;
import java.util.*;

public class RegisteredUserService {
    private RegisteredUser user;
    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private MovieListRepository movieListRepository;
    private RegisteredUserRepository registeredUserRepository;

    public RegisteredUserService(RegisteredUser user,
                                 MovieRepository movieRepository,
                                 UserRepository userRepository,
                                 MovieListRepository movieListRepository,
                                 RegisteredUserRepository registeredUserRepository) {
        this.user = user;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.movieListRepository = movieListRepository;
        this.registeredUserRepository = registeredUserRepository;
    }

    public boolean login(String email, String password) {
        User foundUser = userRepository.getAllUsers().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password))
                .findFirst().orElse(null);

        if (foundUser != null && foundUser instanceof RegisteredUser) {
            this.user = (RegisteredUser)foundUser;
            System.out.println(user.getName() + " logged in successfully!");
            return true;
        }
        System.out.println("Login failed. Invalid email or password.");
        return false;
    }

    // TODO
    // followedUsers, ratedMovies, reviews tables should be added to mydb
    public void followUser(User otherUser) {
        System.out.println(user.getName() + " is now following " + otherUser.getName());
    }

    public void rateMovie(Movie movie, Double rating) {
        System.out.println(user.getName() + " rated the movie \"" + movie.getTitle() + "\" with " + rating);
    }

    public void writeReview(Movie movie, String review) {
        System.out.println(user.getName() + " wrote a review for \"" + movie.getTitle() + "\": " + review);
    }

    public void editReview(Movie movie, String updatedReview) {
        System.out.println(user.getName() + " updated the review for \"" + movie.getTitle() + "\": " + updatedReview);
    }

    public void createList(String name, String description) {
        movieListRepository.createMovieList(user.getId(), name, description);
        System.out.println(user.getName() + " created a list: " + name + " - " + description);
    }

    public void addToList(String listName, Movie movie) {
        MovieList list = movieListRepository.getMovieListByName(user.getId(), listName);
        if (list != null) {
            movieListRepository.addMovieToList(list.getId(), movie.getId());
            list.addMovie(movie);
            System.out.println("\"" + movie.getTitle() + "\" added to the list \"" + listName + "\".");
        } else {
            System.out.println("List \"" + listName + "\" does not exist.");
        }
    }

    public void viewList(String listName) {
        MovieList movieList = movieListRepository.getMovieListByName(user.getId(), listName);
        if (movieList != null) {
            System.out.println(movieList);
        } else {
            System.out.println("List \"" + listName + "\" does not exist.");
        }
    }

    public void addToWatchlist(Movie movie) {
        System.out.println("\"" + movie.getTitle() + "\" added to " + user.getName() + "'s watchlist.");
    }

    public List<Movie> receiveRecommendations(int n) {
        RecommendationEngineRepository recommendationRepo = new RecommendationEngineRepository();
        RecommendationEngine engine = new RecommendationEngine(
                recommendationRepo.getAllUsersMap(),
                recommendationRepo.getAllMoviesMap()
        );

        List<Movie> recommendationsByPreference = engine.analyzeByPreference(user);
        List<Movie> recommendationsByOthers = engine.compareWithOtherUsers(user);

        Set<Movie> recommendationSet = new LinkedHashSet<>();
        recommendationSet.addAll(recommendationsByPreference);
        recommendationSet.addAll(recommendationsByOthers);

        return new ArrayList<>(recommendationSet).subList(0, Math.min(n, recommendationSet.size()));
    }
}
