package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.repository.BookRepository;
import ru.otus.istyazhkina.library.service.BookService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class BookServiceImplTest {

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    void shouldThrowDataOperationExceptionWhileUpdateTitleIfIdNotExists() {
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.updateBookTitle(1, "Random_Title"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Book by provided ID not found");
    }

    @Test
    void shouldThrowDataOperationExceptionIfNoBookByIdFound() {
        Mockito.when(bookRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.getBookById(3))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Book by provided ID not found");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileDeletingNotExistingEntity() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(bookRepository).deleteById(8L);
        assertThatThrownBy(() -> bookService.deleteBookById(8))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("There is no book with provided id");
    }

}