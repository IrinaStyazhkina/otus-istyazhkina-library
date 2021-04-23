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
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
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
    void checkMessageWhileGettingBookByNotExistingId() {
        Mockito.when(bookService.getBookById(1)).thenReturn(null);
        Object res = shell.evaluate(() -> "book by id 1");
        assertThat(res).isEqualTo("Book with id 1 is not found");
    }

    @Test
    void shouldReturnBookNameById() {
        Mockito.when(bookService.getBookById(2)).thenReturn(new Book(2L, "Harry Potter", new Author(2L, "Joanne", "Rowling"), new Genre(1L, "fantasy")));
        Object res = shell.evaluate(() -> "book by id 2");
        assertThat(res).isEqualTo("Harry Potter by Joanne Rowling");
    }

    @Test
    void checkMessageWhileGettingBookByNotExistingTitle() {
        Mockito.when(bookService.getBookByName("not_found")).thenReturn(null);
        Object res = shell.evaluate(() -> "book by title not_found");
        assertThat(res).isEqualTo("Book with title not_found is not found");
    }

    @Test
    void shouldReturnBookIdByTitle() {
        Mockito.when(bookService.getBookByName("1984")).thenReturn(new Book(4L, "1984", new Author(3L, "George", "Orwell"), new Genre(1L, "dystopia")));
        Object res = shell.evaluate(() -> "book by title 1984");
        assertThat(res).isEqualTo("Book's id is 4");
    }

    @Test
    void checkMessageOnDeleteBook() {
        Mockito.when(bookService.deleteBookById(3)).thenReturn(1);
        Object res = shell.evaluate(() -> "delete book 3");
        assertThat(res).isEqualTo("Book is successfully deleted!");
    }

    @Test
    void checkMessageOnDeleteByNotExistingId() {
        Mockito.when(bookService.deleteBookById(30)).thenReturn(0);
        Object res = shell.evaluate(() -> "delete book 30");
        assertThat(res).isEqualTo("Sorry! We can not execute this operation!");
    }

    @Test
    void checkMessageWhileAddingNewBook() {
        Book book = new Book(4L, "1984", new Author(3L, "George", "Orwell"), new Genre(1L, "dystopia"));
        Mockito.when(bookService.addNewBook("1984", "George", "Orwell", "dystopia")).thenReturn(book);
        Object res = shell.evaluate(() -> "add book 1984 George Orwell dystopia");
        assertThat(res).isEqualTo("Book with title 1984 successfully added!");
    }

    @Test
    void checkMessageOnDuplicateDataExceptionWhileAdd() {
        DuplicateDataException e = new DuplicateDataException("Could not insert data in table, because book should be unique!");
        Mockito.when(bookService.addNewBook("Childhood", "Lev", "Tolstoy", "novel")).thenThrow(e);
        Object res = shell.evaluate(() -> "add book Childhood Lev Tolstoy novel");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpDateBook() {
        Book book = new Book(4L, "1984", new Author(3L, "George", "Orwell"), new Genre(1L, "dystopia"));
        Mockito.when(bookService.updateBookTitle(4L, "1984")).thenReturn(book);
        Object res = shell.evaluate(() -> "update book title 4 1984");
        assertThat(res).isEqualTo("Book with id 4 is successfully updated. Book's title is 1984 ");
    }

    @Test
    void checkMessageOnExceptionWhileUpdate() {
        NoDataException e = new NoDataException("Can not update book's title because book with this id is not found");
        Mockito.when(bookService.updateBookTitle(3L, "exception")).thenThrow(e);
        Object res = shell.evaluate(() -> "update book title 3 exception");
        assertThat(res).isEqualTo(e.getMessage());
    }
}