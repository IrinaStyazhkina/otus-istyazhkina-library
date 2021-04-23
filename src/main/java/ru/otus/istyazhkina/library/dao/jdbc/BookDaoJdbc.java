package ru.otus.istyazhkina.library.dao.jdbc;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

@Repository
public class BookDaoJdbc implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    public BookDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int count() {
        return jdbc.getJdbcOperations().queryForObject("select count(*) from books", Integer.class);
    }

    @Override
    public long insert(Book book) {
        try {
            return jdbc.update("insert into books(`title`, `author_id`, `genre_id`) values (:title, :authorId, :genreId)",
                    Map.of("title", book.getTitle(),
                            "authorId", book.getAuthor().getId(),
                            "genreId", book.getGenre().getId()));
        } catch (DuplicateKeyException e) {
            throw new DuplicateDataException("Could not insert data in table, because book should be unique!");
        }
    }

    @Override
    public int update(Book book) throws DuplicateDataException {
        try {
            return jdbc.update("update books set title=:title, author_id =:authorId, genre_id=:genreId where id=:id",
                    Map.of("id", book.getId(),
                            "title", book.getTitle(),
                            "authorId", book.getAuthor().getId(),
                            "genreId", book.getGenre().getId()));
        } catch (DuplicateKeyException e) {
            throw new DuplicateDataException("Could not update data in table, because book should be unique!");
        }

    }

    @Override
    public Book getById(long id) {
        return jdbc.query("select * from books " +
                "inner join authors on books.author_id = authors.id " +
                "inner join genres on books.genre_id = genres.id " +
                "where books.id=:id", singletonMap("id", id), new BookMapper()).stream().findFirst().orElse(null);
    }

    @Override
    public Book getByTitle(String title) {
        return jdbc.query("select * from books " +
                "inner join authors on books.author_id = authors.id " +
                "inner join genres on books.genre_id = genres.id " +
                "where books.title=:title", singletonMap("title", title), new BookMapper()).stream().findFirst().orElse(null);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from books " +
                        "inner join authors on books.author_id = authors.id " +
                        "inner join genres on books.genre_id = genres.id",
                new BookMapper());
    }

    @Override
    public int deleteById(long id) {
        return jdbc.update("delete from books where id=:id", singletonMap("id", id));
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("BOOKS.ID");
            String title = rs.getString("BOOKS.TITLE");
            long authorId = rs.getLong("BOOKS.AUTHOR_ID");
            String authorName = rs.getString("AUTHORS.NAME");
            String authorSurname = rs.getString("AUTHORS.SURNAME");
            long genreId = rs.getLong("BOOKS.GENRE_ID");
            String genreName = rs.getString("GENRES.NAME");

            return new Book(id, title, new Author(authorId, authorName, authorSurname), new Genre(genreId, genreName));
        }
    }
}
