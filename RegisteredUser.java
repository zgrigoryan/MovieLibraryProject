import java.util.List;

public class RegisteredUser extends User {
    private List<Movie> likedMovies;

    public RegisteredUser(Long id, String name, String email, String password, List<Movie> likedMovies) {
        super(id, name, email, password);
        this.likedMovies = likedMovies;
    }

    public List<Movie> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<Movie> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public void likeMovie(Movie movie) {
        likedMovies.add(movie);
        System.out.println(this.getName() + " liked the movie " + movie.getTitle());
    }

    public void unlikeMovie(Movie movie) {
        likedMovies.remove(movie);
        System.out.println(this.getName() + " unliked the movie " + movie.getTitle());
    }
}
