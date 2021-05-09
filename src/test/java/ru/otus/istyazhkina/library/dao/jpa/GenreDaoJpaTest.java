package ru.otus.istyazhkina.library.dao.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(GenreDaoJpa.class)
class GenreDaoJpaTest {

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldReturnGenreForExistingId() {
        Optional<Genre> genre = genreDao.getById(1);
        assertThat(genre).get().isEqualTo(new Genre(1L, "novel"));
    }

    @Test
    void shouldReturnEmptyOptionalIfGenreByIdNotExists() {
        Optional<Genre> genre = genreDao.getById(10);
        assertThat(genre).isEmpty();
    }

    @Test
    void shouldReturnGenreForExistingName() {
        Optional<Genre> genreByName = genreDao.getByName("novel");
        assertThat(genreByName).get().isEqualTo(new Genre(1L, "novel"));
    }

    @Test
    void shouldReturnEmptyOptionalIfGenreByNameNotExists() {
        Optional<Genre> genre = genreDao.getByName("novel123");
        assertThat(genre).isEmpty();
    }

    @Test
    void shouldReturnGenresListForGetAllAuthors() {
        List<Genre> genres = genreDao.getAll();
        assertThat(genres).hasSize(4);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewGenre() {
        Genre genreToInsert = new Genre("criminal");
        genreDao.insert(genreToInsert);
        Long id = testEntityManager.getId(genreToInsert, Long.class);

        Genre insertedGenre = testEntityManager.find(Genre.class, id);
        assertThat(insertedGenre.getName()).isEqualTo("criminal");
    }

    @Test
    void shouldNotInsertGenreWithSameName() {
        Genre genreToInsert = new Genre("novel");
        assertThatThrownBy(() -> genreDao.insert(genreToInsert))
                .isInstanceOf(SameEntityAlreadyExistsException.class)
                .hasMessage("Genre with this name already exists in database");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingGenre() {
        Genre genreToUpdate = new Genre(1L, "novel123");
        assertThat(testEntityManager.find(Genre.class, 1L)).isNotEqualTo(genreToUpdate);

        Genre updatedGenre = genreDao.update(genreToUpdate);
        assertThat(updatedGenre).isEqualTo(genreToUpdate);
    }

    @Test
    void shouldThrowSameEntityAlreadyExistsExceptionWhileUpdateIfGenreWithNewNameExists() {
        Genre genreToUpdate = new Genre(2L, "novel");
        assertThatThrownBy(() -> genreDao.update(genreToUpdate))
                .isInstanceOf(SameEntityAlreadyExistsException.class)
                .hasMessage("Can not update genre because genre with provided name already exists in database");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingGenreIfNoBookIsBound() {
        int modifiedRows = genreDao.deleteGenre(4);
        assertThat(modifiedRows).isEqualTo(1);
        assertThat(testEntityManager.find(Genre.class, 4L)).isNull();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> genreDao.deleteGenre(1))
                .isInstanceOf(ProhibitedDeletionException.class)
                .hasMessage("This operation is not allowed! In system exists book with this genre");
    }

    @Test
    void shouldReturn0IfNoGenreToDelete() {
        int modifiedRows = genreDao.deleteGenre(10);
        assertThat(modifiedRows).isEqualTo(0);
    }
}