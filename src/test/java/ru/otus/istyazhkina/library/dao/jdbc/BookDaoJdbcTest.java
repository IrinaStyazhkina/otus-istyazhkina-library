package ru.otus.istyazhkina.library.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(BookDaoJdbc.class)
class BookDaoJdbcTest {

    @Autowired
    private BookDao bookDao;

    @Test
    void shouldReturnBookForExistingId() {
        final Book book = bookDao.getById(1);
        System.out.println(book);
        assertThat(book).isEqualTo(new Book(1L, "War and Peace", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel")));
    }

    @Test
    void shouldReturnNullIfBookByIdNotExists() {
        Book bookById = bookDao.getById(10);
        assertThat(bookById).isNull();
    }

    @Test
    void shouldReturnBookForExistingTitle() {
        Book book = bookDao.getByTitle("War and Peace");
        assertThat(book).isEqualTo(new Book(1L, "War and Peace", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel")));
    }

    @Test
    void shouldReturnNullIfBookByTitleNotExists() {
        Book book = bookDao.getByTitle("Unknown");
        assertThat(book).isNull();
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        final List<Book> books = bookDao.getAll();
        assertThat(books).hasSize(2);
    }

    @Test
    void shouldInsertNewBookWithExistingAuthorAndGenre() {
        Book bookToInsert = new Book("Anna Karenina", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel"));

        long modifiedRows = bookDao.insert(bookToInsert);
        assertThat(modifiedRows).isEqualTo(1);

        Book bookFromTable = bookDao.getByTitle(bookToInsert.getTitle());
        assertThat(bookFromTable).isNotNull();
    }

    @Test
    void shouldNotInsertBookWithSameTitleAndAuthor() {
        Book bookToInsert = new Book("War and Peace", new Author(1L, "Lev", "Tolstoy"), new Genre(2L, "fantasy"));
        assertThatThrownBy(() -> bookDao.insert(bookToInsert))
                .isInstanceOf(DuplicateDataException.class)
                .hasMessage("Could not insert data in table, because book should be unique!");
    }

    @Test
    void shouldUpdateTitleOfExistingBook() {

        Book bookToUpdate = new Book(1L, "Anna Karenina", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel"));
        assertThat(bookDao.getById(1)).isNotEqualTo(bookToUpdate);

        int modifiedRows = bookDao.update(bookToUpdate);

        assertThat(modifiedRows).isEqualTo(1);
        assertThat(bookDao.getById(1)).isEqualTo(bookToUpdate);
    }

    @Test
    void shouldThrowDuplicateDataExceptionWhileUpdateIfBookWithAuthorAndTitleExists() {
        Book bookToUpdate = new Book(2L, "War and Peace", new Author(1L, "Lev", "Tolstoy"), new Genre(2L, "fantasy"));
        assertThatThrownBy(() -> bookDao.update(bookToUpdate))
                .isInstanceOf(DuplicateDataException.class)
                .hasMessage("Could not update data in table, because book should be unique!");
    }

    @Test
    void shouldDeleteExistingBook() {
        int modifiedRows = bookDao.deleteById(1);
        assertThat(modifiedRows).isEqualTo(1);
        assertThat(bookDao.getById(1)).isNull();
    }

    @Test
    void shouldReturn0IfNoBookToDelete() {
        int modifiedRows = bookDao.deleteById(10);
        assertThat(modifiedRows).isEqualTo(0);
    }

    @Test
    void shouldReturnAllBooksCount() {
        assertThat(bookDao.count()).isEqualTo(2);
    }
}