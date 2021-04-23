package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
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
        assertThat(res).isEqualTo("1\t|\tpoetry \n2\t|\tnovel \n");
    }

    @Test
    void checkMessageWhileGettingGenreByNotExistingId() {
        Mockito.when(genreService.getGenreById(1)).thenReturn(null);
        Object res = shell.evaluate(() -> "genre by id 1");
        assertThat(res).isEqualTo("Genre with id 1 is not found");
    }

    @Test
    void shouldReturnGenreNameById() {
        Mockito.when(genreService.getGenreById(2)).thenReturn(new Genre(2L, "novel"));
        Object res = shell.evaluate(() -> "genre by id 2");
        assertThat(res).isEqualTo("novel");
    }

    @Test
    void checkMessageWhileGettingGenreByNotExistingName() {
        Mockito.when(genreService.getGenreByName("not_found")).thenReturn(null);
        Object res = shell.evaluate(() -> "genre by name not_found");
        assertThat(res).isEqualTo("Genre with name not_found is not found");
    }

    @Test
    void shouldReturnGenreIdByName() {
        Mockito.when(genreService.getGenreByName("found")).thenReturn(new Genre(2L, "novel"));
        Object res = shell.evaluate(() -> "genre by name found");
        assertThat(res).isEqualTo("Genre's id is 2");
    }

    @Test
    void checkMessageOnDeleteGenre() {
        Mockito.when(genreService.deleteGenre(3)).thenReturn(1);
        Object res = shell.evaluate(() -> "delete genre 3");
        assertThat(res).isEqualTo("Genre is successfully deleted!");
    }

    @Test
    void checkMessageOnDeleteByNotExistingId() {
        Mockito.when(genreService.deleteGenre(30)).thenReturn(0);
        Object res = shell.evaluate(() -> "delete genre 30");
        assertThat(res).isEqualTo("Sorry! We can not execute this operation!");
    }

    @Test
    void checkMessageOnConstraintExceptionWhileDelete() {
        ConstraintException e = new ConstraintException("You can not delete this genre because exists book with this genre!");
        Mockito.when(genreService.deleteGenre(20)).thenThrow(e);
        Object res = shell.evaluate(() -> "delete genre 20");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageWhileAddingNewGenre() {
        Genre genre = new Genre(5L, "detective");
        Mockito.when(genreService.addNewGenre("detective")).thenReturn(genre);
        Object res = shell.evaluate(() -> "add genre detective");
        assertThat(res).isEqualTo("Genre with name detective successfully added!");
    }

    @Test
    void checkMessageOnDuplicateDataExceptionWhileAdd() {
        DuplicateDataException e = new DuplicateDataException("Could not insert data in table, because genre should be unique!");
        Mockito.when(genreService.addNewGenre("exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "add genre exception");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpDateGenre() {
        Genre genre = new Genre(2L, "fiction");
        Mockito.when(genreService.updateGenresName(2L, "fiction")).thenReturn(genre);
        Object res = shell.evaluate(() -> "update genre 2 fiction");
        assertThat(res).isEqualTo("Genre with id 2 successfully updated. Genre name is fiction");
    }

    @Test
    void checkMessageOnExceptionWhileUpdate() {
        NoDataException e = new NoDataException("Can not update genre's name because genre with this id is not found");
        Mockito.when(genreService.updateGenresName(3L, "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "update genre 3 exception");
        assertThat(res).isEqualTo(e.getMessage());
    }
}