DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS authors;

CREATE TABLE genres
(
    id   BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE authors
(
    id      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255),
    surname VARCHAR(255),
    UNIQUE (name, surname)
);

CREATE TABLE books
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(255),
    author_id BIGINT NOT NULL,
    genre_id  BIGINT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES authors (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id),
    UNIQUE (title, author_id)
);