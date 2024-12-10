CREATE TABLE IF NOT EXISTS actor (
    id BIGSERIAL PRIMARY KEY,
    firstName TEXT,
    lastName TEXT,
    dateOfBirth DATE,
    awards TEXT
);

CREATE TABLE IF NOT EXISTS def_genre (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS movie (
    id BIGSERIAL PRIMARY KEY,
    title TEXT,
    releaseDate DATE,
    director TEXT,
    average_rating DECIMAL(3, 2)
    );

CREATE TABLE IF NOT EXISTS n2n_movie_to_genre (
    movie_id BIGSERIAL NOT NULL,
    genre_id BIGSERIAL NOT NULL,
    PRIMARY KEY (movie_id, genre_id),
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES def_genre(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS n2n_movie_to_actor (
    movie_id BIGSERIAL NOT NULL,
    actor_id BIGSERIAL NOT NULL,
    PRIMARY KEY (movie_id, actor_id),
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE,
    FOREIGN KEY (actor_id) REFERENCES actor(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS "user" (
    id BIGSERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE IF NOT EXISTS def_role (
    id BIGSERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE IF NOT EXISTS n2n_user_to_role (
    user_id BIGSERIAL NOT NULL,
    role_id BIGSERIAL NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES def_role(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS user_watchlist (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL,
    movie_id BIGSERIAL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS movielist (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL,
    name TEXT,
    description TEXT,
    FOREIGN KEY (user_id) REFERENCES "user"(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS n2n_list_to_movie (
    id BIGSERIAL PRIMARY KEY,
    LIST_ID BIGSERIAL NOT NULL,
    movie_id BIGSERIAL NOT NULL,
    FOREIGN KEY (LIST_ID) REFERENCES movielist(id) ON DELETE CASCADE,
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
);