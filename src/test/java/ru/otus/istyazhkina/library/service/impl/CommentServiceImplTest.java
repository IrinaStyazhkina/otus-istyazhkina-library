package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.istyazhkina.library.dao.CommentDao;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.CommentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CommentServiceImplTest {

    @MockBean
    private CommentDao commentDao;

    @Autowired
    private CommentService commentService;

    @Test
    void shouldThrowDataOperationExceptionWhileUpdateTitleIfIdNotExists() {
        Mockito.when(commentDao.getById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> commentService.updateCommentContent(1, "Random_Content"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update comment. Comment by provided ID not found in database.");
    }

}