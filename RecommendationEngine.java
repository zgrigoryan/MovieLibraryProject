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
        Map<Movie, Integer> recommendationScores = new HashMap<>();

        allUsers.values().stream()
                .filter(otherUser -> otherUser instanceof RegisteredUser)
                .filter(otherUser -> !otherUser.equals(user))
                .map(otherUser -> (RegisteredUser) otherUser)
                .forEach(otherRegisteredUser ->
                        processRecommendations(registeredUser, otherRegisteredUser, recommendationScores)
                );

        return recommendationScores.entrySet().stream()
                .sorted(Map.Entry.<Movie, Integer>comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private void processRecommendations(RegisteredUser registeredUser, RegisteredUser otherRegisteredUser,
                                        Map<Movie, Integer> recommendationScores) {

        List<Movie> userLikedMovies = registeredUser.getLikedMovies();
        List<Movie> otherUserLikedMovies = otherRegisteredUser.getLikedMovies();

        boolean hasCommonMovies = otherUserLikedMovies.stream()
                .anyMatch(userLikedMovies::contains);

        if (hasCommonMovies) {
            otherUserLikedMovies.stream()
                    .filter(movie -> !userLikedMovies.contains(movie))
                    .forEach(movie ->
                            recommendationScores.merge(movie, 1, Integer::sum)
                    );
        }
    }
}
