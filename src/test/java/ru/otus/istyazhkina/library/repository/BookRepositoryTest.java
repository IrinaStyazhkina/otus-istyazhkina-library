package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldReturnBookForExistingId() {
        Optional<Book> actualBook = bookRepository.findById(1L);
        Book expectedBook = testEntityManager.find(Book.class, 1L);
        assertThat(actualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @Test
    void shouldReturnEmptyOptionalIfBookByIdNotExists() {
        Optional<Book> book = bookRepository.findById(10L);
        assertThat(book).isEmpty();
    }

    @Test
    void shouldReturnBookForExistingTitle() {
        List<Book> books = bookRepository.findByTitle("War and Peace");
        assertThat(books).contains(new Book(1L, "War and Peace", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel")));
    }

    @Test
    void shouldReturnEmptyListIfBookByTitleNotExists() {
        assertThat(bookRepository.findByTitle("Unknown")).isEmpty();
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        final List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewBookWithExistingAuthorAndGenre() {
        Book bookToInsert = new Book("Anna Karenina", new Author(1L, "Lev", "Tolstoy"), new Genre(1L, "novel"));
        bookRepository.save(bookToInsert);
        assertThat(bookToInsert.getId()).isGreaterThan(0);

        Book insertedBook = testEntityManager.find(Book.class, bookToInsert.getId());
        assertThat(insertedBook).usingRecursiveComparison().isEqualTo(bookToInsert);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateTitleOfExistingBook() {
        Book book = testEntityManager.find(Book.class, 1L);
        String oldTitle = book.getTitle();
        testEntityManager.detach(book);

        String newTitle = "Anna Karenina";
        book.setTitle(newTitle);
        bookRepository.saveAndFlush(book);

        Book updatedBook = testEntityManager.find(Book.class, book.getId());
        assertThat(updatedBook.getTitle()).isNotEqualTo(oldTitle).isEqualTo(newTitle);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingBook() {
        Book book = testEntityManager.find(Book.class, 1L);
        assertThat(book).isNotNull();
        testEntityManager.detach(book);

        bookRepository.deleteById(1L);
        assertThat(testEntityManager.find(Book.class, 1L)).isNull();
    }

    @Test
    void shouldThrowExceptionIfNoBookToDelete() {
        assertThatThrownBy(() -> {
            bookRepository.deleteById(10L);
            bookRepository.flush();
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldReturnAllBooksCount() {
        assertThat(bookRepository.count()).isEqualTo(3L);
    }
}