import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {

    private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String USER = "admin";
    private static final String PASSWORD = "password";

    private static final String CREATE_ACTOR_TABLE = "CREATE TABLE IF NOT EXISTS actor (" +
            "id SERIAL PRIMARY KEY, " +
            "firstName TEXT, " +
            "lastName TEXT, " +
            "dateOfBirth DATE, " +
            "awards TEXT" +
            ");";

    private static final String CREATE_DEF_GENRE_TABLE = "CREATE TABLE IF NOT EXISTS def_genre (" +
            "id SERIAL PRIMARY KEY, " +
            "name TEXT" +
            ");";

    private static final String CREATE_MOVIE_TABLE = "CREATE TABLE IF NOT EXISTS movie (" +
            "id SERIAL PRIMARY KEY, " +
            "title TEXT, " +
            "releaseDate DATE, " +
            "director TEXT, " +
            "average_rating DECIMAL(3, 2)" +
            ");";

    private static final String CREATE_N2N_MOVIE_TO_GENRE_TABLE = "CREATE TABLE IF NOT EXISTS n2n_movie_to_genre (" +
            "movie_id INT NOT NULL, " +
            "genre_id INT NOT NULL, " +
            "PRIMARY KEY (movie_id, genre_id), " +
            "FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (genre_id) REFERENCES def_genre(id) ON DELETE CASCADE" +
            ");";

    private static final String CREATE_N2N_MOVIE_TO_ACTOR_TABLE = "CREATE TABLE IF NOT EXISTS n2n_movie_to_actor (" +
            "movie_id INT NOT NULL, " +
            "actor_id INT NOT NULL, " +
            "PRIMARY KEY (movie_id, actor_id), " +
            "FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (actor_id) REFERENCES actor(id) ON DELETE CASCADE" +
            ");";

    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS \"user\" (" +
            "id SERIAL PRIMARY KEY, " +
            "name TEXT, " +
            "email TEXT, " +
            "password TEXT" +
            ");";

    private static final String CREATE_DEF_ROE_TABLE = "CREATE TABLE IF NOT EXISTS def_roe (" +
            "id SERIAL PRIMARY KEY, " +
            "name TEXT" +
            ");";

    private static final String CREATE_N2N_USER_TO_ROLE_TABLE = "CREATE TABLE IF NOT EXISTS n2n_user_to_role (" +
            "user_id INT NOT NULL, " +
            "role_id INT NOT NULL, " +
            "PRIMARY KEY (user_id, role_id), " +
            "FOREIGN KEY (user_id) REFERENCES \"user\"(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (role_id) REFERENCES def_roe(id) ON DELETE CASCADE" +
            ");";

    private static final String CREATE_USER_WATCHLIST_TABLE = "CREATE TABLE IF NOT EXISTS user_watchlist (" +
            "id SERIAL PRIMARY KEY, " +
            "user_id INT NOT NULL, " +
            "movie_id INT NOT NULL, " +
            "FOREIGN KEY (user_id) REFERENCES \"user\"(id) ON DELETE CASCADE, " +
            "FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE" +
            ");";

    private static Connection connect() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("PostgreSQL driver not found", e);
        }
    }

    public static void createTables() {
        try (Connection connection = connect(); Statement statement = connection.createStatement()) {

            statement.executeUpdate(CREATE_ACTOR_TABLE);
            System.out.println("Actor table created successfully.");

            statement.executeUpdate(CREATE_DEF_GENRE_TABLE);
            System.out.println("Def_genre table created successfully.");

            statement.executeUpdate(CREATE_MOVIE_TABLE);
            System.out.println("Movie table created successfully.");

            statement.executeUpdate(CREATE_N2N_MOVIE_TO_GENRE_TABLE);
            System.out.println("N2N_movie_to_genre table created successfully.");

            statement.executeUpdate(CREATE_N2N_MOVIE_TO_ACTOR_TABLE);
            System.out.println("N2N_movie_to_actor table created successfully.");

            statement.executeUpdate(CREATE_USER_TABLE);
            System.out.println("User table created successfully.");

            statement.executeUpdate(CREATE_DEF_ROE_TABLE);
            System.out.println("Def_roe table created successfully.");

            statement.executeUpdate(CREATE_N2N_USER_TO_ROLE_TABLE);
            System.out.println("N2N_user_to_role table created successfully.");

            statement.executeUpdate(CREATE_USER_WATCHLIST_TABLE);
            System.out.println("User_watchlist table created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        createTables();
    }
}
