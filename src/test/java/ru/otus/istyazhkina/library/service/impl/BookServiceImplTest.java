package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
import ru.otus.istyazhkina.library.service.BookService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class BookServiceImplTest {

    @MockBean
    private BookDao bookDao;

    @Autowired
    private BookService bookService;

    @Test
    void shouldThrowNoDataExceptionWhileUpdateTitleIfIdNotExists() {
        Mockito.when(bookDao.getById(1)).thenReturn(null);
        assertThatThrownBy(() -> bookService.updateBookTitle(1, "Random_Title"))
                .isInstanceOf(NoDataException.class)
                .hasMessage("Can not update book's title because book with this id is not found");
    }


}