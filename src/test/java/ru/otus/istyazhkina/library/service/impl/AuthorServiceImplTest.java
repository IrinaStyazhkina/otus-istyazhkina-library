package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class AuthorServiceImplTest {

    @MockBean
    private AuthorDao authorDao;

    @Autowired
    private AuthorService authorService;

    @Test
    void shouldThrowDataOperationExceptionWhileUpdateNameIfIdNotExists() {
        Mockito.when(authorDao.getById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authorService.updateAuthor(1, "Random_Name", "Random Surname"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update author. Author by provided ID not found in database.");
    }
}