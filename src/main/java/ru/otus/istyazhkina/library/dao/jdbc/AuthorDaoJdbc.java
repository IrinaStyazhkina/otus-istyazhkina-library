package ru.otus.istyazhkina.library.dao.jdbc;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

@Repository
public class AuthorDaoJdbc implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public long insert(Author author) throws DuplicateDataException {
        try {
            return jdbc.update("insert into authors(`name`, `surname`) values(:name, :surname)",
                    Map.of("name", author.getName(),
                            "surname", author.getSurname()));
        } catch (DuplicateKeyException e) {
            throw new DuplicateDataException("Could not insert data in table, because author should be unique!");
        }
    }

    @Override
    public Author getById(long id) {
        return jdbc.query("select * from authors where id = :id", singletonMap("id", id), new AuthorMapper()).stream().findFirst().orElse(null);
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select * from authors", new AuthorMapper());
    }

    @Override
    public int update(Author author) throws DuplicateDataException {
        try {
            return jdbc.update("update authors set name=:name, surname=:surname where id=:id",
                    Map.of("id", author.getId(),
                            "name", author.getName(),
                            "surname", author.getSurname()));
        } catch (DuplicateKeyException e) {
            throw new DuplicateDataException("Could not update data in table, because author should be unique!");
        }

    }

    @Override
    public int deleteById(long id) throws ConstraintException {
        try {
            return jdbc.update("delete from authors where id=:id", singletonMap("id", id));
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException("You can not delete this author because exists book with this author");
        }
    }

    @Override
    public Author getByName(String name, String surname) {
        return jdbc.query("select * from authors where name=:name AND surname=:surname", Map.of("name", name, "surname", surname), new AuthorMapper()).stream().findFirst().orElse(null);
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("ID");
            String name = rs.getString("NAME");
            String surname = rs.getString("SURNAME");
            return new Author(id, name, surname);
        }
    }
}
