package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GenreCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private GenreService genreService;

    @Test
    void checkMessageOnEmptyResultList() {
        Mockito.when(genreService.getAllGenres()).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "all genres");
        assertThat(res).isEqualTo("No data in table 'Genres'");
    }

    @Test
    void shouldReturnGenres() {
        Mockito.when(genreService.getAllGenres()).thenReturn(List.of(new Genre(1L, "poetry"), new Genre(2L, "novel")));
        Object res = shell.evaluate(() -> "all genres");
        assertThat(res).isEqualTo("1\t|\tpoetry\n" +
                "2\t|\tnovel\n");
    }

    @Test
    void checkMessageWhileGettingGenreByNotExistingId() throws DataOperationException {
        DataOperationException e = new DataOperationException("Genre by provided ID not found in database");
        Mockito.when(genreService.getGenreById(1)).thenThrow(e);
        Object res = shell.evaluate(() -> "genre by id 1");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnGenreById() throws DataOperationException {
        Mockito.when(genreService.getGenreById(2)).thenReturn(new Genre(2L, "novel"));
        Object res = shell.evaluate(() -> "genre by id 2");
        assertThat(res).isEqualTo("2\t|\tnovel");
    }

    @Test
    void checkMessageWhileGettingGenreByNotExistingName() throws DataOperationException {
        DataOperationException e = new DataOperationException("No Genre found by name not_found");
        Mockito.when(genreService.getGenreByName("not_found")).thenThrow(e);
        Object res = shell.evaluate(() -> "genre by name not_found");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnGenreByName() throws DataOperationException {
        Mockito.when(genreService.getGenreByName("found")).thenReturn(new Genre(2L, "novel"));
        Object res = shell.evaluate(() -> "genre by name found");
        assertThat(res).isEqualTo("2\t|\tnovel");
    }

    @Test
    void checkMessageOnDeleteGenre() throws DataOperationException {
        Mockito.doNothing().when(genreService).deleteGenre(3);
        Object res = shell.evaluate(() -> "delete genre 3");
        assertThat(res).isEqualTo("Genre is successfully deleted!");
    }

    @Test
    void checkMessageWhileExceptionOnDelete() throws DataOperationException {
        Mockito.doThrow(new DataOperationException("There is no genre with provided id")).when(genreService).deleteGenre(30);
        Object res = shell.evaluate(() -> "delete genre 30");
        assertThat(res).isEqualTo("There is no genre with provided id");
    }

    @Test
    void checkMessageWhileAddingNewGenre() throws DataOperationException {
        Genre genre = new Genre(5L, "detective");
        Mockito.when(genreService.addNewGenre("detective")).thenReturn(genre);
        Object res = shell.evaluate(() -> "add genre detective");
        assertThat(res).isEqualTo("Genre with name detective successfully added!");
    }

    @Test
    void checkMessageOnExceptionWhileAdd() throws DataOperationException {
        DataOperationException e = new DataOperationException("Genre with this name already exists in database");
        Mockito.when(genreService.addNewGenre("exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "add genre exception");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpdateGenre() throws DataOperationException {
        Genre genre = new Genre(2L, "fiction");
        Mockito.when(genreService.updateGenresName(2L, "fiction")).thenReturn(genre);
        Object res = shell.evaluate(() -> "update genre 2 fiction");
        assertThat(res).isEqualTo("Genre with id 2 successfully updated. Genre name is fiction");
    }

    @Test
    void checkMessageOnExceptionWhileUpdate() throws DataOperationException {
        DataOperationException e = new DataOperationException("Can not update genre because genre with provided name already exists in database");
        Mockito.when(genreService.updateGenresName(3L, "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "update genre 3 exception");
        assertThat(res).isEqualTo(e.getMessage());
    }
}