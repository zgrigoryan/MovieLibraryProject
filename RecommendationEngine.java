import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {

    private Map<Long, User> allUsers;
    private Map<Long, Movie> allMovies;

    public RecommendationEngine(Map<Long, User> allUsers, Map<Long, Movie> allMovies) {
        this.allUsers = allUsers;
        this.allMovies = allMovies;
    }

    public List<Movie> analyzeByPreference(User user) {
        if (!(user instanceof RegisteredUser)) {
            return Collections.emptyList();
        }

        RegisteredUser registeredUser = (RegisteredUser) user;
        List<Movie> likedMovies = registeredUser.getLikedMovies();

        Set<Genre> preferredGenres = likedMovies.stream()
                .flatMap(movie -> movie.getGenre().stream())
                .collect(Collectors.toSet());

        List<Movie> recommendedMovies = allMovies.values().stream()
                .filter(movie -> !likedMovies.contains(movie))
                .filter(movie -> !Collections.disjoint(movie.getGenre(), preferredGenres))
                .collect(Collectors.toList());

        return recommendedMovies;
    }

    public List<Movie> compareWithOtherUsers(User user) {
        if (!(user instanceof RegisteredUser)) {
            return Collections.emptyList();
        }

        RegisteredUser registeredUser = (RegisteredUser) user;
        List<Movie> userLikedMovies = registeredUser.getLikedMovies();

        Map<Movie, Integer> recommendationScores = new HashMap<>();

        for (User otherUser : allUsers.values()) {
            if (otherUser.equals(user) || !(otherUser instanceof RegisteredUser)) {
                continue;
            }

            RegisteredUser otherRegisteredUser = (RegisteredUser) otherUser;
            List<Movie> otherUserLikedMovies = otherRegisteredUser.getLikedMovies();

            Set<Movie> commonMovies = new HashSet<>(userLikedMovies);
            commonMovies.retainAll(otherUserLikedMovies);

            if (!commonMovies.isEmpty()) {
                for (Movie movie : otherUserLikedMovies) {
                    if (!userLikedMovies.contains(movie)) {
                        recommendationScores.put(movie, recommendationScores.getOrDefault(movie, 0) + 1);
                    }
                }
            }
        }

        return recommendationScores.entrySet().stream()
                .sorted(Map.Entry.<Movie, Integer>comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
