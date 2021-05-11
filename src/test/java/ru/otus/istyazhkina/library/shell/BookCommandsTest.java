package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.BookService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class BookCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private BookService bookService;

    @Test
    void checkMessageOnEmptyResultList() {
        Mockito.when(bookService.getAllBooks()).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "all books");
        assertThat(res).isEqualTo("No data in table 'Books'");
    }

    @Test
    void shouldReturnBooks() {
        Mockito.when(bookService.getAllBooks()).thenReturn(List.of
                (new Book(1L, "Anna Karenina", new Author(1L, "Lev", "Tolstoy"), new Genre(2L, "novel")),
                        new Book(2L, "Harry Potter", new Author(2L, "Joanne", "Rowling"), new Genre(1L, "fantasy"))));
        Object res = shell.evaluate(() -> "all books");
        assertThat(res).isEqualTo("1\t|\tAnna Karenina\t|\tLev Tolstoy\t|\tnovel\n2\t|\tHarry Potter\t|\tJoanne Rowling\t|\tfantasy\n");
    }

    @Test
    void checkMessageWhileGettingBookByNotExistingId() throws DataOperationException {
        Mockito.when(bookService.getBookById(1)).thenThrow(new DataOperationException("Book by provided ID not found in database"));
        Object res = shell.evaluate(() -> "book by id 1");
        assertThat(res).isEqualTo("Book by provided ID not found in database");
    }

    @Test
    void shouldReturnBookNameById() throws DataOperationException {
        Mockito.when(bookService.getBookById(2)).thenReturn(new Book(2L, "Harry Potter", new Author(2L, "Joanne", "Rowling"), new Genre(1L, "fantasy")));
        Object res = shell.evaluate(() -> "book by id 2");
        assertThat(res).isEqualTo("Harry Potter");
    }

    @Test
    void checkMessageWhileGettingBookByNotExistingTitle() {
        Mockito.when(bookService.getBooksByTitle("not_found")).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "book by title not_found");
        assertThat(res).isEqualTo("No books found by provided title");
    }

    @Test
    void shouldReturnBookIdByTitle() {
        Mockito.when(bookService.getBooksByTitle("1984")).thenReturn(List.of(new Book(4L, "1984", new Author(3L, "George", "Orwell"), new Genre(1L, "dystopia"))));
        Object res = shell.evaluate(() -> "book by title 1984");
        assertThat(res).isEqualTo("Id's of books with provided title:\n4");
    }

    @Test
    void checkMessageOnDeleteBook() throws DataOperationException {
        Mockito.doNothing().when(bookService).deleteBookById(3);
        Object res = shell.evaluate(() -> "delete book 3");
        assertThat(res).isEqualTo("Book is successfully deleted!");
    }

    @Test
    void checkMessageOnDeleteByNotExistingId() throws DataOperationException {
        Mockito.doThrow(new DataOperationException("There is no book with provided id")).when(bookService).deleteBookById(30);
        Object res = shell.evaluate(() -> "delete book 30");
        assertThat(res).isEqualTo("There is no book with provided id");
    }

    @Test
    void checkMessageWhileAddingNewBook() {
        Book book = new Book(4L, "1984", new Author(3L, "George", "Orwell"), new Genre(1L, "dystopia"));
        Mockito.when(bookService.addNewBook("1984", "George", "Orwell", "dystopia")).thenReturn(book);
        Object res = shell.evaluate(() -> "add book 1984 George Orwell dystopia");
        assertThat(res).isEqualTo("Book with title 1984 successfully added!");
    }


    @Test
    void checkMessageOnUpDateBook() throws DataOperationException {
        Book book = new Book(4L, "1984", new Author(3L, "George", "Orwell"), new Genre(1L, "dystopia"));
        Mockito.when(bookService.updateBookTitle(4L, "1984")).thenReturn(book);
        Object res = shell.evaluate(() -> "update book title 4 1984");
        assertThat(res).isEqualTo("Book with id 4 is successfully updated. Book's title is 1984 ");
    }

    @Test
    void checkMessageOnExceptionWhileUpdate() throws DataOperationException {
        DataOperationException e = new DataOperationException("Book by provided ID not found in database");
        Mockito.when(bookService.updateBookTitle(3L, "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "update book title 3 exception");
        assertThat(res).isEqualTo(e.getMessage());
    }
}