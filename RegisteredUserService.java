import java.util.*;

public class RegisteredUserService {
    private RegisteredUser user;
    private Map<Long, Movie> allMovies;
    private Map<Long, List<Movie>> userWatchlists;
    private Map<Long, List<String>> userReviews;
    private Map<Long, Map<Long, Double>> movieRatings;
    private Map<Long, List<User>> userFollowers;

    public RegisteredUserService(RegisteredUser user, Map<Long, Movie> allMovies) {
        this.user = user;
        this.allMovies = allMovies;
        this.userWatchlists = new HashMap<>();
        this.userReviews = new HashMap<>();
        this.userFollowers = new HashMap<>();
        this.movieRatings = new HashMap<>();
    }

    public boolean login(String email, String password) {
        if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password)) {
            System.out.println(user.getName() + " logged in successfully!");
            return true; // successful login
        }
        System.out.println("Login failed. Invalid email or password.");
        return false; // login failed
    }

    public void followUser(User otherUser) {
        if (!userFollowers.containsKey(user.getId())) {
            userFollowers.put(user.getId(), new ArrayList<>());
        }
        userFollowers.get(user.getId()).add(otherUser);
        System.out.println(user.getName() + " is now following " + otherUser.getName());
    }

    public void rateMovie(Movie movie, Double rating) {
        if (movieRatings.containsKey(movie.getId())) {
            movieRatings.get(movie.getId()).put(user.getId(), rating);
        } else {
            Map<Long, Double> ratings = new HashMap<>();
            ratings.put(user.getId(), rating);
            movieRatings.put(movie.getId(), ratings);
        }
        System.out.println(user.getName() + " rated the movie " + movie.getTitle() + " with " + rating);
    }

    public void writeReview(Movie movie, String review) {
        if (!userReviews.containsKey(movie.getId())) {
            userReviews.put(movie.getId(), new ArrayList<>());
        }
        userReviews.get(movie.getId()).add(review);
        System.out.println(user.getName() + " wrote a review for " + movie.getTitle() + ": " + review);
    }

    public void editReview(Movie movie, String updatedReview) {
        if (userReviews.containsKey(movie.getId()) && !userReviews.get(movie.getId()).isEmpty()) {
            userReviews.get(movie.getId()).set(0, updatedReview); // Replace the first review (assuming one review per movie)
            System.out.println(user.getName() + " updated the review for " + movie.getTitle() + ": " + updatedReview);
        } else {
            System.out.println("No review found to edit for " + movie.getTitle());
        }
    }

    public void createList(String name, String description) {
        System.out.println(user.getName() + " created a list: " + name + " - " + description);
    }

    public void addToWatchlist(Movie movie) {
        if (!userWatchlists.containsKey(user.getId())) {
            userWatchlists.put(user.getId(), new ArrayList<>());
        }
        userWatchlists.get(user.getId()).add(movie);
        System.out.println(movie.getTitle() + " added to " + user.getName() + "'s watchlist.");
    }

    // TODO
    public List<Movie> receiveRecommendations() {
        List<Movie> allMovieList = new ArrayList<>(allMovies.values());
        Collections.shuffle(allMovieList); // random recommendations
        return allMovieList.subList(0, Math.min(3, allMovieList.size()));
    }
}
