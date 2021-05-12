package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.repository.CommentRepository;
import ru.otus.istyazhkina.library.service.CommentService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class CommentServiceImplTest {

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    void shouldThrowDataOperationExceptionWhileUpdateTitleIfIdNotExists() {
        Mockito.when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> commentService.updateCommentContent(1, "Random_Content"))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Can not update comment. Comment by provided ID not found");
    }

    @Test
    void shouldThrowDataOperationExceptionIfNoCommentByIdFound() {
        Mockito.when(commentRepository.findById(3L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> commentService.getCommentById(3))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("Comment by provided ID not found");
    }

    @Test
    void shouldThrowDataOperationExceptionWhileDeletingNotExistingEntity() {
        Mockito.doThrow(EmptyResultDataAccessException.class).when(commentRepository).deleteById(8L);
        assertThatThrownBy(() -> commentService.deleteComment(8))
                .isInstanceOf(DataOperationException.class)
                .hasMessage("There is no comment with provided id");
    }
}