package ru.otus.istyazhkina.library.dao.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(AuthorDaoJdbc.class)
class AuthorDaoJdbcTest {

    @Autowired
    private AuthorDao authorDao;

    @Test
    void shouldReturnAuthorForExistingId() {
        Author authorById = authorDao.getById(1);
        assertThat(authorById).isEqualTo(new Author(1L, "Lev", "Tolstoy"));
    }

    @Test
    void shouldReturnNullIfAuthorByIdNotExists() {
        Author authorById = authorDao.getById(10);
        assertThat(authorById).isNull();
    }

    @Test
    void shouldReturnAuthorForExistingName() {
        Author authorByName = authorDao.getByName("Lev", "Tolstoy");
        assertThat(authorByName).isEqualTo(new Author(1L, "Lev", "Tolstoy"));
    }

    @Test
    void shouldReturnNullIfAuthorByNameNotExists() {
        final Author authorByName = authorDao.getByName("Ivan", "Ivanov");
        assertThat(authorByName).isNull();
    }

    @Test
    void shouldReturnAuthorListForGetAllAuthors() {
        List<Author> authors = authorDao.getAll();
        assertThat(authors).hasSize(3);
    }

    @Test
    void shouldInsertNewAuthor() {
        Author authorToInsert = new Author("Petr", "Petrov");
        long modifiedRows = authorDao.insert(authorToInsert);
        assertThat(modifiedRows).isEqualTo(1);

        Author authorFromTable = authorDao.getByName(authorToInsert.getName(), authorToInsert.getSurname());
        assertThat(authorFromTable).isNotNull();
    }

    @Test
    void shouldNotInsertAuthorWithSameNameAndSurname() {
        Author authorToInsert = new Author("Lev", "Tolstoy");
        assertThatThrownBy(() -> authorDao.insert(authorToInsert))
                .isInstanceOf(DuplicateDataException.class)
                .hasMessage("Could not insert data in table, because author should be unique!");
    }

    @Test
    void shouldUpdateExistingAuthor() {
        Author authorToUpdate = new Author(1L, "Alexandr", "Pushkin");
        assertThat(authorDao.getById(1)).isNotEqualTo(authorToUpdate);

        int modifiedRows = authorDao.update(authorToUpdate);

        assertThat(modifiedRows).isEqualTo(1);
        assertThat(authorDao.getById(1)).isEqualTo(authorToUpdate);
    }

    @Test
    void shouldThrowDuplicateDataExceptionWhileUpdateIfAuthorWithNewNameExists() {
        Author authorToUpdate = new Author(2L, "Lev", "Tolstoy");
        assertThatThrownBy(() -> authorDao.update(authorToUpdate))
                .isInstanceOf(DuplicateDataException.class)
                .hasMessage("Could not update data in table, because author should be unique!");
    }

    @Test
    void shouldDeleteExistingAuthorIfNoBookIsBound() {
        int modifiedRows = authorDao.deleteById(3);
        assertThat(modifiedRows).isEqualTo(1);
        assertThat(authorDao.getById(3)).isNull();
    }

    @Test
    void shouldNotDeleteExistingAuthorIfBookIsBound() {
        assertThatThrownBy(() -> authorDao.deleteById(1))
                .isInstanceOf(ConstraintException.class)
                .hasMessage("You can not delete this author because exists book with this author");
    }

    @Test
    void shouldReturn0IfNoAuthorToDelete() {
        int modifiedRows = authorDao.deleteById(10);
        assertThat(modifiedRows).isEqualTo(0);
    }
}