package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.repository.GenreRepository;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class GenreServiceImplTest {

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    @Test
    void shouldThrowNoDataExceptionWhileUpdateIfIdNotExists() {
        Mockito.when(genreRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> genreService.updateGenresName(1, "Random_Name"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update genre. Genre by provided ID not found");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileUpdateIfGenreAlreadyExists() {
        Mockito.when(genreRepository.findById(2L)).thenReturn(Optional.of(new Genre(2L, "fantasy")));
        Mockito.when(genreRepository.saveAndFlush(new Genre(2L, "novel"))).thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> genreService.updateGenresName(2, "novel"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update genre because genre with same name already exists!");
    }

    @Test
    void shouldThrowDataOperationExceptionIfNoGenreByIdFound() {
        Mockito.when(genreRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> genreService.getGenreById(3))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("No genre found by provided id");
    }

    @Test
    void shouldThrowDataOperationExceptionIfNoGenreByNameFound() {
        Mockito.when(genreRepository.findByName("science fiction")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> genreService.getGenreByName("science fiction"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("No genre found by provided name");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileAddIfSameGenreAlreadyExists() {
        Mockito.when(genreRepository.save(new Genre("poetry"))).thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> genreService.addNewGenre("poetry"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not add genre because genre already exists!");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileDeletingNotExistingEntity() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(genreRepository).deleteById(8L);
        assertThatThrownBy(() -> genreService.deleteGenre(8))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("There is no genre with provided id");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileDeletingGenreBoundWithBook() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(genreRepository).deleteById(6L);
        assertThatThrownBy(() -> genreService.deleteGenre(6))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("You can not delete genre because exists book with this genre!");
    }
}