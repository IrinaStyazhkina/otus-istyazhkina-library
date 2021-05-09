package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private AuthorService authorService;

    @Test
    void checkMessageOnEmptyResultList() {
        Mockito.when(authorService.getAllAuthors()).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "all authors");
        assertThat(res).isEqualTo("No data in table 'Authors'");
    }

    @Test
    void shouldReturnAuthors() {
        Mockito.when(authorService.getAllAuthors()).thenReturn(List.of(new Author(1L, "Ivan", "Ivanov"), new Author(2L, "Petr", "Petrov")));
        Object res = shell.evaluate(() -> "all authors");
        assertThat(res).isEqualTo("1\t|\tIvan\t|\tIvanov\n2\t|\tPetr\t|\tPetrov\n");
    }

    @Test
    void checkMessageWhileGettingAuthorByNotExistingId() throws DataOperationException {
        DataOperationException e = new DataOperationException("Author by provided ID not found in database");
        Mockito.when(authorService.getAuthorById(1)).thenThrow(e);
        Object res = shell.evaluate(() -> "author by id 1");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnAuthorNameById() throws DataOperationException {
        Mockito.when(authorService.getAuthorById(2)).thenReturn(new Author(2L, "Alexander", "Pushkin"));
        Object res = shell.evaluate(() -> "author by id 2");
        assertThat(res).isEqualTo("Alexander Pushkin");
    }

    @Test
    void checkMessageWhileGettingAuthorByNotExistingName() throws DataOperationException {
        DataOperationException e = new DataOperationException("No Author found by name Vasya Veselov");
        Mockito.when(authorService.getAuthorByName("Vasya", "Veselov")).thenThrow(e);
        Object res = shell.evaluate(() -> "author by name Vasya Veselov");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnAuthorIdByName() throws DataOperationException {
        Mockito.when(authorService.getAuthorByName("Alexander", "Pushkin")).thenReturn(new Author(2L, "Alexander", "Pushkin"));
        Object res = shell.evaluate(() -> "author by name Alexander Pushkin");
        assertThat(res).isEqualTo("Author's id is 2");
    }

    @Test
    void checkMessageOnDeleteAuthor() throws DataOperationException {
        Mockito.when(authorService.deleteAuthor(3)).thenReturn(1);
        Object res = shell.evaluate(() -> "delete author 3");
        assertThat(res).isEqualTo("Author is successfully deleted!");
    }

    @Test
    void checkMessageOnDeleteByNotExistingId() throws DataOperationException {
        Mockito.when(authorService.deleteAuthor(30)).thenReturn(0);
        Object res = shell.evaluate(() -> "delete author 30");
        assertThat(res).isEqualTo("Deletion is not successful. Please check if provided author id exists");
    }

    @Test
    void checkMessageOnConstraintWhileDelete() throws DataOperationException {
        DataOperationException e = new DataOperationException("This operation is not allowed! In system exists book with this author");
        Mockito.when(authorService.deleteAuthor(20)).thenThrow(e);
        Object res = shell.evaluate(() -> "delete author 20");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageWhileAddingNewAuthor() throws DataOperationException {
        Author author = new Author(5L, "Fedor", "Dostoevskiy");
        Mockito.when(authorService.addNewAuthor("Fedor", "Dostoevskiy")).thenReturn(author);
        Object res = shell.evaluate(() -> "add author Fedor Dostoevskiy");
        assertThat(res).isEqualTo("Author with name Fedor Dostoevskiy successfully added!");
    }

    @Test
    void checkMessageIfSameEntityAlreadyExistsWhileAdd() throws DataOperationException {
        DataOperationException e = new DataOperationException("Author with this name and surname already exists in database");
        Mockito.when(authorService.addNewAuthor("duplicate", "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "add author duplicate exception");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpDateAuthorName() throws DataOperationException {
        Author author = new Author(2L, "Aleksey", "Tolstoy");
        Mockito.when(authorService.updateAuthor(2L, "Aleksey", "Tolstoy")).thenReturn(author);
        Object res = shell.evaluate(() -> "update author 2 Aleksey Tolstoy");
        assertThat(res).isEqualTo("Author with id 2 successfully updated. Author's name is Aleksey Tolstoy");
    }

    @Test
    void checkMessageOnExceptionWhileUpdateName() throws DataOperationException {
        DataOperationException e = new DataOperationException("Author by provided ID not found in database");
        Mockito.when(authorService.updateAuthor(3L, "Ivan", "Ivanov")).thenThrow(e);
        Object res = shell.evaluate(() -> "update author 3 Ivan Ivanov");
        assertThat(res).isEqualTo(e.getMessage());
    }
}