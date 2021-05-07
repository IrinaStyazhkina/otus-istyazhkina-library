package ru.otus.istyazhkina.library.dao.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BookDaoJpa.class)
class BookDaoJpaTest {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldReturnBookForExistingId() {
        Optional<Book> book = bookDao.getById(1);
        assertThat(book).get().isEqualTo(new Book(1L, "War and Peace", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel")));
    }

    @Test
    void shouldReturnEmptyOptionalIfBookByIdNotExists() {
        Optional<Book> book = bookDao.getById(10);
        assertThat(book).isEmpty();
    }

    @Test
    void shouldReturnBookForExistingTitle() {
        List<Book> books = bookDao.getByTitle("War and Peace");
        assertThat(books).contains(new Book(1L, "War and Peace", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel")));
    }

    @Test
    void shouldReturnEmptyListIfBookByTitleNotExists() {
        assertThat(bookDao.getByTitle("Unknown")).isEmpty();
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        final List<Book> books = bookDao.getAll();
        assertThat(books).hasSize(3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewBookWithExistingAuthorAndGenre() {
        Book bookToInsert = new Book("Anna Karenina", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel"));
        bookDao.insert(bookToInsert);
        Long id = testEntityManager.getId(bookToInsert, Long.class);

        Book insertedBook = testEntityManager.find(Book.class, id);
        assertThat(insertedBook).isEqualTo(bookToInsert);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateTitleOfExistingBook() {
        Book bookToUpdate = testEntityManager.find(Book.class, 1L);
        testEntityManager.detach(bookToUpdate);
        bookToUpdate.setTitle("Anna Karenina");
        assertThat(testEntityManager.find(Book.class, 1L)).isNotEqualTo(bookToUpdate);

        Book updatedBook = bookDao.update(bookToUpdate);
        assertThat(updatedBook).isEqualTo(bookToUpdate);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingBook() {
        int modifiedRows = bookDao.deleteById(1);
        assertThat(modifiedRows).isEqualTo(1);
        assertThat(testEntityManager.find(Book.class, 1L)).isNull();
    }

    @Test
    void shouldReturn0IfNoBookToDelete() {
        int modifiedRows = bookDao.deleteById(10);
        assertThat(modifiedRows).isEqualTo(0);
    }

    @Test
    void shouldReturnAllBooksCount() {
        assertThat(bookDao.count()).isEqualTo(3L);
    }
}