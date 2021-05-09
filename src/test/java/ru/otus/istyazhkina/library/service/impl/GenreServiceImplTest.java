package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class GenreServiceImplTest {

    @MockBean
    private GenreDao genreDao;

    @Autowired
    private GenreService genreService;

    @Test
    void shouldThrowNoDataExceptionWhileUpdateIfIdNotExists() {
        Mockito.when(genreDao.getById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> genreService.updateGenresName(1, "Random_Name"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update genre. Genre by provided ID not found in database.");
    }
}