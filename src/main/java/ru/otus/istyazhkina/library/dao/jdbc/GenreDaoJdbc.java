package ru.otus.istyazhkina.library.dao.jdbc;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;

@Repository
public class GenreDaoJdbc implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoJdbc(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int insert(Genre genre) throws DuplicateKeyException {
        try {
            return jdbc.update("insert into genres(`name`) values(:name)", singletonMap("name", genre.getName()));
        } catch (DuplicateKeyException e) {
            throw new DuplicateDataException("Could not insert data in table, because genre should be unique!");
        }
    }

    @Override
    public int update(Genre genre) throws DuplicateDataException {
        try {
            return jdbc.update("update genres set name=:name where id=:id",
                    Map.of("id", genre.getId(),
                            "name", genre.getName()));
        } catch (DuplicateKeyException e) {
            throw new DuplicateDataException("Could not update data in table, because genre should be unique!");
        }
    }

    @Override
    public Genre getById(long id) {
        return jdbc.query("select id, name from genres where id=:id", singletonMap("id", id), new GenreMapper()).stream().findFirst().orElse(null);
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select id, name from genres", new GenreMapper());
    }

    @Override
    public int deleteById(long id) throws ConstraintException {
        try {
            return jdbc.update("delete from genres where id=:id", singletonMap("id", id));
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintException("You can not delete this genre because exists book with this genre");
        }
    }

    @Override
    public Genre getByName(String name) {
        return jdbc.query("select id, name from genres where name=:name", singletonMap("name", name), new GenreMapper()).stream().findFirst().orElse(null);
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("ID");
            String name = rs.getString("NAME");
            return new Genre(id, name);
        }
    }
}
