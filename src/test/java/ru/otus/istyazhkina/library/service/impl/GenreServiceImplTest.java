package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
import ru.otus.istyazhkina.library.service.GenreService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class GenreServiceImplTest {

    @MockBean
    private GenreDao genreDao;

    @Autowired
    private GenreService genreService;

    @Test
    void shouldThrowNoDataExceptionWHileUpdateIfIdNotExists() {
        Mockito.when(genreDao.getById(1)).thenReturn(null);
        assertThatThrownBy(() -> genreService.updateGenresName(1, "Random_Name"))
                .isInstanceOf(NoDataException.class)
                .hasMessage("Can not update genre's name because genre with this id is not found");
    }
}