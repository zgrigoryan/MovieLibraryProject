import java.util.*;

public class RegisteredUserService {
    private RegisteredUser user;
    private Map<Long, Movie> allMovies;
    private Map<Long, User> allUsers;
    private Map<Long, List<Movie>> userWatchlists;
    private Map<Long, Map<Long, String>> userReviews;
    private Map<Long, Map<Long, Double>> movieRatings;
    private Map<Long, List<User>> userFollowers;
    private Map<Long, Map<String, CustomList>> userCustomLists;

    public RegisteredUserService(RegisteredUser user, Map<Long, Movie> allMovies, Map<Long, User> allUsers) {
        this.user = user;
        this.allMovies = allMovies;
        this.allUsers = allUsers;
        this.userWatchlists = new HashMap<>();
        this.userReviews = new HashMap<>();
        this.movieRatings = new HashMap<>();
        this.userFollowers = new HashMap<>();
        this.userCustomLists = new HashMap<>();
    }

    public boolean login(String email, String password) {
        if (user != null && user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password)) {
            System.out.println(user.getName() + " logged in successfully!");
            return true;
        }
        System.out.println("Login failed. Invalid email or password.");
        return false;
    }

    public void followUser(User otherUser) {
        userFollowers.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(otherUser);
        System.out.println(user.getName() + " is now following " + otherUser.getName());
    }

    public void rateMovie(Movie movie, Double rating) {
        movieRatings.computeIfAbsent(movie.getId(), k -> new HashMap<>()).put(user.getId(), rating);
        System.out.println(user.getName() + " rated the movie \"" + movie.getTitle() + "\" with " + rating);

        Map<Long, Double> ratings = movieRatings.get(movie.getId());
        double averageRating = ratings.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        movie.setAverageRating(averageRating);
    }

    public void writeReview(Movie movie, String review) {
        userReviews.computeIfAbsent(movie.getId(), k -> new HashMap<>()).put(user.getId(), review);
        System.out.println(user.getName() + " wrote a review for \"" + movie.getTitle() + "\": " + review);
    }

    public void editReview(Movie movie, String updatedReview) {
        Map<Long, String> reviews = userReviews.get(movie.getId());
        if (reviews != null && reviews.containsKey(user.getId())) {
            reviews.put(user.getId(), updatedReview);
            System.out.println(user.getName() + " updated the review for \"" + movie.getTitle() + "\": " + updatedReview);
        } else {
            System.out.println("No existing review found for \"" + movie.getTitle() + "\" to update.");
        }
    }

    public void createList(String name, String description) {
        Map<String, CustomList> userLists = userCustomLists.computeIfAbsent(user.getId(), k -> new HashMap<>());
        if (userLists.containsKey(name)) {
            System.out.println("A list with the name \"" + name + "\" already exists.");
        } else {
            CustomList customList = new CustomList(name, description);
            userLists.put(name, customList);
            System.out.println(user.getName() + " created a list: " + name + " - " + description);
        }
    }
    public void addToList(String listName, Movie movie) {
        Map<String, CustomList> userLists = userCustomLists.get(user.getId());
        if (userLists != null && userLists.containsKey(listName)) {
            CustomList customList = userLists.get(listName);
            customList.addMovie(movie);
            System.out.println("\"" + movie.getTitle() + "\" added to the list \"" + listName + "\".");
        } else {
            System.out.println("List \"" + listName + "\" does not exist.");
        }
    }
    public void viewList(String listName) {
        Map<String, CustomList> userLists = userCustomLists.get(user.getId());
        if (userLists != null && userLists.containsKey(listName)) {
            CustomList customList = userLists.get(listName);
            System.out.println(customList);
        } else {
            System.out.println("List \"" + listName + "\" does not exist.");
        }
    }

    public void addToWatchlist(Movie movie) {
        userWatchlists.computeIfAbsent(user.getId(), k -> new ArrayList<>()).add(movie);
        System.out.println("\"" + movie.getTitle() + "\" added to " + user.getName() + "'s watchlist.");
    }

    public List<Movie> receiveRecommendations(int n) {
        RecommendationEngine engine = new RecommendationEngine(allUsers, allMovies);

        List<Movie> recommendationsByPreference = engine.analyzeByPreference(user);
        List<Movie> recommendationsByOthers = engine.compareWithOtherUsers(user);

        Set<Movie> recommendationSet = new LinkedHashSet<>();
        recommendationSet.addAll(recommendationsByPreference);
        recommendationSet.addAll(recommendationsByOthers);

        // n represents the number of movie recommendations user wants to receive
        return new ArrayList<>(recommendationSet).subList(0, Math.min(n, recommendationSet.size()));
    }
}
