import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Movie {
    private Long id;
    private String title;
    private List<Genre> genre;
    private String director;
    private Date releaseDate;
    private Double averageRating;
    private List<Actor> cast;

    public Movie(Long id, String title, List<Genre> genre, String director, Date releaseDate, Double averageRating, List<Actor> cast){
        this.title = title;
        this.id = id;
        this.genre = genre;
        this.director = director;
        this.releaseDate = releaseDate;
        this.averageRating = averageRating;
        this.cast = cast;
    }
    public Movie(Long id, String title, List<Genre> genre, String director, Date releaseDate, List<Actor> cast){
        new Movie(id, title, genre, director, releaseDate, 0.0, cast);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public List<Genre> getGenre() {
        return genre;
    }

    public void setGenre(List<Genre> genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
