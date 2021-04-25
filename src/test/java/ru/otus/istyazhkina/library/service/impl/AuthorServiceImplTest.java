package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
import ru.otus.istyazhkina.library.service.AuthorService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class AuthorServiceImplTest {

    @MockBean
    private AuthorDao authorDao;

    @Autowired
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        Mockito.when(authorDao.getById(1)).thenReturn(null);
    }

    @Test
    void shouldThrowNoDataExceptionWhileUpdateNameIfIdNotExists() {
        assertThatThrownBy(() -> authorService.updateAuthorsName(1, "Random_Name"))
                .isInstanceOf(NoDataException.class)
                .hasMessage("Can not update author's name because author with this id is not found");
    }

    @Test
    void shouldThrowNoDataExceptionWhileUpdateSurnameIfIdNotExists() {
        assertThatThrownBy(() -> authorService.updateAuthorsSurname(1, "Random_Surname"))
                .isInstanceOf(NoDataException.class)
                .hasMessage("Can not update author's surname because author with this id is not found");
    }


}