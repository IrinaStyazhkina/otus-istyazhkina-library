package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldReturnAuthorForExistingId() {
        Optional<Author> actualAuthor = authorRepository.findById(1L);
        Author expectedAuthor = testEntityManager.find(Author.class, 1L);
        assertThat(actualAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @Test
    void shouldReturnEmptyOptionalIfAuthorByIdNotExists() {
        Optional<Author> author = authorRepository.findById(10L);
        assertThat(author).isEmpty();
    }

    @Test
    void shouldReturnAuthorForExistingName() {
        Optional<Author> authorByName = authorRepository.findByNameAndSurname("Lev", "Tolstoy");
        assertThat(authorByName).get().isEqualTo(new Author(1L, "Lev", "Tolstoy"));
    }

    @Test
    void shouldThrowReturnEmptyOptionalIfAuthorByNameNotExists() {
        Optional<Author> author = authorRepository.findByNameAndSurname("Ivan", "Ivanov");
        assertThat(author).isEmpty();
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(4);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewAuthor() {
        Author author = new Author("Petr", "Petrov");
        authorRepository.save(author);

        assertThat(author.getId()).isGreaterThan(0);
        Author actualAuthor = testEntityManager.find(Author.class, author.getId());

        assertThat(actualAuthor)
                .matches(a -> a.getName().equals("Petr"))
                .matches(a -> a.getSurname().equals("Petrov"));
    }

    @Test
    void shouldNotInsertAuthorWithSameNameAndSurname() {
        Author authorToInsert = new Author("Lev", "Tolstoy");
        assertThatThrownBy(() -> authorRepository.save(authorToInsert))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingAuthor() {
        Author author = testEntityManager.find(Author.class, 1L);
        String oldName = author.getName();
        String oldSurname = author.getSurname();
        testEntityManager.detach(author);

        String newName = "Aleksander";
        String newSurname = "Pushkin";
        author.setName(newName);
        author.setSurname(newSurname);
        authorRepository.saveAndFlush(author);

        assertThat(testEntityManager.find(Author.class, 1L))
                .matches(a -> a.getName().equals(newName))
                .matches(a -> a.getSurname().equals(newSurname));
    }

    @Test
    void shouldThrowExceptionWhileUpdateIfAuthorWithNewNameExists() {
        Author authorToUpdate = new Author(2L, "Lev", "Tolstoy");
        assertThatThrownBy(() -> authorRepository.saveAndFlush(authorToUpdate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingAuthorIfNoBookIsBound() {
        Author author = testEntityManager.find(Author.class, 4L);
        assertThat(author).isNotNull();
        testEntityManager.detach(author);

        authorRepository.deleteById(4L);
        authorRepository.flush();
        assertThat(testEntityManager.find(Author.class, 4L)).isNull();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> {
            authorRepository.deleteById(1L);
            authorRepository.flush();
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldThrowExceptionIfNoAuthorToDelete() {
        assertThatThrownBy(() -> {
            authorRepository.deleteById(10L);
            authorRepository.flush();
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }
}