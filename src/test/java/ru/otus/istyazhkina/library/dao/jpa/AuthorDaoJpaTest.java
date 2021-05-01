package ru.otus.istyazhkina.library.dao.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(AuthorDaoJpa.class)
class AuthorDaoJpaTest {

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void shouldReturnAuthorForExistingId() {
        Optional<Author> author = authorDao.getById(1);
        assertThat(author.get()).isEqualTo(new Author(1L, "Lev", "Tolstoy"));
    }

    @Test
    void shouldReturnNullIfAuthorByIdNotExists() {
        Optional<Author> author = authorDao.getById(10);
        assertThat(author).isEmpty();
    }

    @Test
    void shouldReturnAuthorForExistingName() {
        Author authorByName = authorDao.getByName("Lev", "Tolstoy");
        assertThat(authorByName).isEqualTo(new Author(1L, "Lev", "Tolstoy"));
    }

    @Test
    void shouldThrowNoEntityFoundInDataBaseExceptionIfAuthorByNameNotExists() {
        assertThatThrownBy(() -> authorDao.getByName("Ivan", "Ivanov"))
                .isInstanceOf(NoEntityFoundInDataBaseException.class)
                .hasMessage("No Author found by name Ivan Ivanov");
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        List<Author> authors = authorDao.getAll();
        assertThat(authors).hasSize(4);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewAuthor() {
        Author authorToInsert = new Author("Petr", "Petrov");
        authorDao.insert(authorToInsert);

        Long id = testEntityManager.getId(authorToInsert, Long.class);

        Author insertedAuthor = testEntityManager.find(Author.class, id);
        assertThat(insertedAuthor.getName()).isEqualTo("Petr");
        assertThat(insertedAuthor.getSurname()).isEqualTo("Petrov");
    }

    @Test
    void shouldNotInsertAuthorWithSameNameAndSurname() {
        Author authorToInsert = new Author("Lev", "Tolstoy");
        assertThatThrownBy(() -> authorDao.insert(authorToInsert))
                .isInstanceOf(SameEntityAlreadyExistsException.class)
                .hasMessage("Author with this name and surname already exists in database");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingAuthor() {
        Author authorToUpdate = new Author(1L, "Alexandr", "Pushkin");
        assertThat(testEntityManager.find(Author.class, 1L)).isNotEqualTo(authorToUpdate);

        Author updatedAuthor = authorDao.update(authorToUpdate);
        assertThat(updatedAuthor).isEqualTo(authorToUpdate);
    }

    @Test
    void shouldThrowSameEntityAlreadyExistsExceptionWhileUpdateIfAuthorWithNewNameExists() {
        Author authorToUpdate = new Author(2L, "Lev", "Tolstoy");
        assertThatThrownBy(() -> authorDao.update(authorToUpdate))
                .isInstanceOf(SameEntityAlreadyExistsException.class)
                .hasMessage("Can not update author because author with provided name and surname already exists in database");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingAuthorIfNoBookIsBound() {
        int modifiedRows = authorDao.deleteAuthor(4);
        assertThat(modifiedRows).isEqualTo(1);
        assertThat(testEntityManager.find(Author.class, 4L)).isNull();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> authorDao.deleteAuthor(1))
                .isInstanceOf(ProhibitedDeletionException.class)
                .hasMessage("This operation is not allowed! In system exists book with this author");
    }

    @Test
    void shouldReturn0IfNoAuthorToDelete() {
        int modifiedRows = authorDao.deleteAuthor(10);
        assertThat(modifiedRows).isEqualTo(0);
    }
}