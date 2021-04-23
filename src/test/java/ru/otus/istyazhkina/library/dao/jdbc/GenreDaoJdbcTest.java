package ru.otus.istyazhkina.library.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(GenreDaoJdbc.class)
class GenreDaoJdbcTest {

    @Autowired
    GenreDao genreDao;

    @Test
    void shouldReturnGenreForExistingId() {
        Genre genreById = genreDao.getById(1);
        assertThat(genreById).isEqualTo(new Genre(1L, "novel"));
    }

    @Test
    void shouldReturnNullIfGenreByIdNotExists() {
        Genre genreById = genreDao.getById(10);
        assertThat(genreById).isNull();
    }

    @Test
    void shouldReturnGenreForExistingName() {
        Genre genreByName = genreDao.getByName("novel");
        assertThat(genreByName).isEqualTo(new Genre(1L, "novel"));
    }

    @Test
    void shouldReturnNullIfGenreByNameNotExists() {
        Genre genreByName = genreDao.getByName("novel123");
        assertThat(genreByName).isNull();
    }

    @Test
    void shouldReturnGenresListForGetAllAuthors() {
        List<Genre> genres = genreDao.getAll();
        assertThat(genres).hasSize(3);
    }

    @Test
    void shouldInsertNewGenre() {
        Genre genreToInsert = new Genre("criminal");
        long modifiedRows = genreDao.insert(genreToInsert);
        assertThat(modifiedRows).isEqualTo(1);

        Genre genreFromTable = genreDao.getByName(genreToInsert.getName());
        assertThat(genreFromTable).isNotNull();
    }

    @Test
    void shouldNotInsertGenreWithSameName() {
        Genre genreToInsert = new Genre("novel");
        assertThatThrownBy(() -> genreDao.insert(genreToInsert))
                .isInstanceOf(DuplicateDataException.class)
                .hasMessage("Could not insert data in table, because genre should be unique!");
    }

    @Test
    void shouldUpdateExistingGenre() {
        Genre genreToUpdate = new Genre(1L, "novel123");
        assertThat(genreDao.getById(1)).isNotEqualTo(genreToUpdate);

        int modifiedRows = genreDao.update(genreToUpdate);

        assertThat(modifiedRows).isEqualTo(1);
        assertThat(genreDao.getById(1)).isEqualTo(genreToUpdate);
    }

    @Test
    void shouldThrowDuplicateDataExceptionWhileUpdateIfGenreWithNewNameExists() {
        Genre genreToUpdate = new Genre(2L, "novel");
        assertThatThrownBy(() -> genreDao.update(genreToUpdate))
                .isInstanceOf(DuplicateDataException.class)
                .hasMessage("Could not update data in table, because genre should be unique!");
    }

    @Test
    void shouldDeleteExistingGenreIfNoBookIsBound() {
        int modifiedRows = genreDao.deleteById(2);
        assertThat(modifiedRows).isEqualTo(1);
        assertThat(genreDao.getById(2)).isNull();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> genreDao.deleteById(1))
                .isInstanceOf(ConstraintException.class)
                .hasMessage("You can not delete this genre because exists book with this genre");
    }

    @Test
    void shouldReturn0IfNoGenreToDelete() {
        int modifiedRows = genreDao.deleteById(10);
        assertThat(modifiedRows).isEqualTo(0);
    }

}