package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.repository.AuthorRepository;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class AuthorServiceImplTest {

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorService authorService;

    @Test
    void shouldThrowDataOperationExceptionWhileUpdateNameIfIdNotExists() {
        Mockito.when(authorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authorService.updateAuthor(1, "Random_Name", "Random Surname"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update author. Author by provided ID not found");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileUpdateIfAuthorAlreadyExists() {
        Mockito.when(authorRepository.findById(2L)).thenReturn(Optional.of(new Author(2L, "Alexey", "Tolstoy")));
        Mockito.when(authorRepository.saveAndFlush(new Author(2L, "Lev", "Tolstoy"))).thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> authorService.updateAuthor(2, "Lev", "Tolstoy"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update author because author with same name already exists!");
    }

    @Test
    void shouldThrowDataOperationExceptionIfNoAuthorByIdFound() {
        Mockito.when(authorRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authorService.getAuthorById(3))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Author by provided ID not found");
    }

    @Test
    void shouldThrowDataOperationExceptionIfNoAuthorByNameFound() {
        Mockito.when(authorRepository.findByNameAndSurname("Ivan", "Ivanov")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authorService.getAuthorByName("Ivan", "Ivanov"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("No author found by provided name");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileAddIfSameAuthorAlreadyExists() {
        Mockito.when(authorRepository.save(new Author("Ivan", "Turgenev"))).thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> authorService.addNewAuthor("Ivan", "Turgenev"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not add author because author already exists!");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileDeletingNotExistingEntity() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(authorRepository).deleteById(8L);
        assertThatThrownBy(() -> authorService.deleteAuthor(8))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("There is no author with provided id");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileDeletingAuthorBoundWithBook() {
        Mockito.doThrow(DataIntegrityViolationException.class).when(authorRepository).deleteById(6L);
        assertThatThrownBy(() -> authorService.deleteAuthor(6))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("You can not delete author because exists book with this author!");
    }
}