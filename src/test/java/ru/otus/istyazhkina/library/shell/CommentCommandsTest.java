package ru.otus.istyazhkina.library.shell;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.CommentService;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CommentCommandsTest {

    @Autowired
    private Shell shell;

    @MockBean
    private CommentService commentService;

    private final Book existingBook = new Book(1L, "Anna Karenina", new Author(1L, "Lev", "Tolstoy"), new Genre(2L, "novel"));

    @Test
    void checkMessageOnEmptyResultList() {
        Mockito.when(commentService.getAllComments()).thenReturn(Collections.EMPTY_LIST);
        Object res = shell.evaluate(() -> "all comments");
        assertThat(res).isEqualTo("No data in table 'Comments'");
    }

    @Test
    void shouldReturnComments() {
        Mockito.when(commentService.getAllComments()).thenReturn(List.of(new Comment(1L, "comment1", existingBook), new Comment(2L, "comment2", existingBook)));
        Object res = shell.evaluate(() -> "all comments");
        assertThat(res).isEqualTo("1\t|\tcomment1\t|\tAnna Karenina\n2\t|\tcomment2\t|\tAnna Karenina\n");
    }

    @Test
    void checkMessageWhileGettingCommentByNotExistingId() throws DataOperationException {
        DataOperationException e = new DataOperationException("Comment by provided ID not found in database");
        Mockito.when(commentService.getCommentById(1)).thenThrow(e);
        Object res = shell.evaluate(() -> "comment by id 1");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnCommentById() throws DataOperationException {
        Mockito.when(commentService.getCommentById(2)).thenReturn(new Comment(2L, "test comment", existingBook));
        Object res = shell.evaluate(() -> "comment by id 2");
        assertThat(res).isEqualTo("2\t|\ttest comment\t|\tAnna Karenina");
    }

//    @Test
//    void checkMessageOnDeleteComment() {
//        Mockito.when(commentService.deleteComment(3)).thenReturn(1);
//        Object res = shell.evaluate(() -> "delete comment 3");
//        assertThat(res).isEqualTo("Comment is successfully deleted!");
//    }
//
//    @Test
//    void checkMessageOnDeleteByNotExistingId() {
//        Mockito.when(commentService.deleteComment(30)).thenReturn(0);
//        Object res = shell.evaluate(() -> "delete comment 30");
//        assertThat(res).isEqualTo("Deletion is not successful. Please check if provided comment id exists");
//    }

    @Test
    void checkMessageWhileAddingNewComment() throws DataOperationException {
        Mockito.when(commentService.addNewComment("test", 1L)).thenReturn(new Comment("test", existingBook));
        Object res = shell.evaluate(() -> "add comment test 1");
        assertThat(res).isEqualTo("Comment successfully added!");
    }

    @Test
    void checkMessageWhileAddingNewCommentForNotExistingBook() throws DataOperationException {
        DataOperationException e = new DataOperationException("Can not add new Comment. Book by provided id is not found!\"");
        Mockito.when(commentService.addNewComment("test", 30L)).thenThrow(e);
        Object res = shell.evaluate(() -> "add comment test 30");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void checkMessageOnUpdateComment() throws DataOperationException {
        Comment comment = new Comment(2L, "test_comment", existingBook);
        Mockito.when(commentService.updateCommentContent(2L, "test_comment")).thenReturn(comment);
        Object res = shell.evaluate(() -> "update comment 2 test_comment");
        assertThat(res).isEqualTo("Comment with id 2 successfully updated. Comment is test_comment");
    }

    @Test
    void checkMessageOnExceptionWhileUpdate() throws DataOperationException {
        DataOperationException e = new DataOperationException("Can not update comment. Comment by provided ID not found in database.");
        Mockito.when(commentService.updateCommentContent(3L, "test")).thenThrow(e);
        Object res = shell.evaluate(() -> "update comment 3 test");
        assertThat(res).isEqualTo(e.getMessage());
    }

    @Test
    void shouldReturnCommentsByBookId() {
        Mockito.when(commentService.getCommentsByBookId(1L)).thenReturn(List.of(new Comment(1L, "comment1", existingBook), new Comment(2L, "comment2", existingBook)));
        Object res = shell.evaluate(() -> "comments by book id 1");
        assertThat(res).isEqualTo("Comments:\n" +
                "1\t|\tcomment1\t|\tAnna Karenina\n" +
                "2\t|\tcomment2\t|\tAnna Karenina\n");
    }
}