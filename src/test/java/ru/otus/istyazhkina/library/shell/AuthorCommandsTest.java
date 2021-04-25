package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
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
    void checkMessageWhileGettingAuthorByNotExistingId() {
        Mockito.when(authorService.getAuthorById(1)).thenReturn(null);
        Object res = shell.evaluate(() -> "author by id 1");
        assertThat(res).isEqualTo("No author with id 1 found");
    }

    @Test
    void shouldReturnAuthorNameById() {
        Mockito.when(authorService.getAuthorById(2)).thenReturn(new Author(2L, "Alexander", "Pushkin"));
        Object res = shell.evaluate(() -> "author by id 2");
        assertThat(res).isEqualTo("Alexander Pushkin");
    }

    @Test
    void checkMessageWhileGettingAuthorByNotExistingName() {
        Mockito.when(authorService.getAuthorByName("Vasya", "Veselov")).thenReturn(null);
        Object res = shell.evaluate(() -> "author by name Vasya Veselov");
        assertThat(res).isEqualTo("No author with name Vasya Veselov found");
    }

    @Test
    void shouldReturnAuthorIdByName() {
        Mockito.when(authorService.getAuthorByName("Alexander", "Pushkin")).thenReturn(new Author(2L, "Alexander", "Pushkin"));
        Object res = shell.evaluate(() -> "author by name Alexander Pushkin");
        assertThat(res).isEqualTo("Author's id is 2");
    }

    @Test
    void checkMessageOnDeleteAuthor() {
        Mockito.when(authorService.deleteAuthor(3)).thenReturn(1);
        Object res = shell.evaluate(() -> "delete author 3");
        assertThat(res).isEqualTo("Author is successfully deleted!");
    }

    @Test
    void checkMessageOnDeleteByNotExistingId() {
        Mockito.when(authorService.deleteAuthor(30)).thenReturn(0);
        Object res = shell.evaluate(() -> "delete author 30");
        assertThat(res).isEqualTo("Sorry! We can not execute this operation!");
    }

    @Test
    void checkMessageOnConstraintExceptionWhileDelete() {
        ConstraintException e = new ConstraintException("You can not delete this author because exists book with this author!");
        Mockito.when(authorService.deleteAuthor(20)).thenThrow(e);
        Object res = shell.evaluate(() -> "delete author 20");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageWhileAddingNewAuthor() {
        Author author = new Author(5L, "Fedor", "Dostoevskiy");
        Mockito.when(authorService.addNewAuthor("Fedor", "Dostoevskiy")).thenReturn(author);
        Object res = shell.evaluate(() -> "add author Fedor Dostoevskiy");
        assertThat(res).isEqualTo("Author with name Fedor Dostoevskiy successfully added!");
    }

    @Test
    void checkMessageOnDuplicateDataExceptionWhileAdd() {
        DuplicateDataException e = new DuplicateDataException("Could not insert data in table, because author should be unique!");
        Mockito.when(authorService.addNewAuthor("duplicate", "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "add author duplicate exception");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpDateAuthorName() {
        Author author = new Author(2L, "Aleksey", "Tolstoy");
        Mockito.when(authorService.updateAuthorsName(2L, "Aleksey")).thenReturn(author);
        Object res = shell.evaluate(() -> "update author name 2 Aleksey");
        assertThat(res).isEqualTo("Author with id 2 successfully updated. Author's name is Aleksey Tolstoy");
    }

    @Test
    void checkMessageOnExceptionWhileUpdateName() {
        NoDataException e = new NoDataException("Can not update author's name because author with this id is not found");
        Mockito.when(authorService.updateAuthorsName(3L, "Ivan")).thenThrow(e);
        Object res = shell.evaluate(() -> "update author name 3 Ivan");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpDateAuthorSurname() {
        Author author = new Author(2L, "Aleksey", "Tolstoy");
        Mockito.when(authorService.updateAuthorsSurname(2L, "Tolstoy")).thenReturn(author);
        Object res = shell.evaluate(() -> "update author surname 2 Tolstoy");
        assertThat(res).isEqualTo("Author with id 2 successfully updated. Author's name is Aleksey Tolstoy");
    }

    @Test
    void checkMessageOnExceptionWhileUpdateSurname() {
        NoDataException e = new NoDataException("Can not update author's surname because author with this id is not found");
        Mockito.when(authorService.updateAuthorsSurname(3L, "Turgenev")).thenThrow(e);
        Object res = shell.evaluate(() -> "update author surname 3 Turgenev");
        assertThat(res).isEqualTo(e.getMessage());
    }

}