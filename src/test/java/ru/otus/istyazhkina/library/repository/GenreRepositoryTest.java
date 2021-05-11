package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldReturnGenreForExistingId() {
        Optional<Genre> actualGenre = genreRepository.findById(1L);
        Genre expectedGenre = testEntityManager.find(Genre.class, 1L);
        assertThat(actualGenre).isPresent().get().usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @Test
    void shouldReturnEmptyOptionalIfGenreByIdNotExists() {
        Optional<Genre> genre = genreRepository.findById(10L);
        assertThat(genre).isEmpty();
    }

    @Test
    void shouldReturnGenreForExistingName() {
        Optional<Genre> genreByName = genreRepository.findByName("novel");
        assertThat(genreByName).get().isEqualTo(new Genre(1L, "novel"));
    }

    @Test
    void shouldReturnEmptyOptionalIfGenreByNameNotExists() {
        Optional<Genre> genre = genreRepository.findByName("novel123");
        assertThat(genre).isEmpty();
    }

    @Test
    void shouldReturnGenresListForGetAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        assertThat(genres).hasSize(4);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewGenre() {
        Genre genreToInsert = new Genre("criminal");
        genreRepository.save(genreToInsert);

        assertThat(genreToInsert.getId()).isGreaterThan(0);
        Genre actualGenre = testEntityManager.find(Genre.class, genreToInsert.getId());

        assertThat(actualGenre.getName()).isEqualTo("criminal");
    }

    @Test
    void shouldNotInsertGenreWithSameName() {
        Genre genreToInsert = new Genre("novel");
        assertThatThrownBy(() -> genreRepository.save(genreToInsert))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingGenre() {
        Genre genre = testEntityManager.find(Genre.class, 1L);
        String oldName = genre.getName();
        testEntityManager.detach(genre);

        String newName = "novel123";
        genre.setName(newName);
        genreRepository.saveAndFlush(genre);

        Genre updatedGenre = testEntityManager.find(Genre.class, genre.getId());
        assertThat(updatedGenre.getName()).isNotEqualTo(oldName).isEqualTo(newName);
    }

    @Test
    void shouldThrowExceptionWhileUpdateIfGenreWithNewNameExists() {
        Genre genreToUpdate = new Genre(2L, "novel");
        assertThatThrownBy(() -> genreRepository.saveAndFlush(genreToUpdate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingGenreIfNoBookIsBound() {
        Genre genre = testEntityManager.find(Genre.class, 4L);
        assertThat(genre).isNotNull();
        testEntityManager.detach(genre);

        genreRepository.deleteById(4L);
        genreRepository.flush();
        assertThat(testEntityManager.find(Genre.class, 4L)).isNull();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> {
            genreRepository.deleteById(1L);
            genreRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldThrowExceptionIfNoGenreToDelete() {
        assertThatThrownBy(() -> {
            genreRepository.deleteById(10L);
            genreRepository.flush();
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }
}