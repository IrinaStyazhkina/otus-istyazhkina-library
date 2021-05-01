package ru.otus.istyazhkina.library.dao.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.dao.CommentDao;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(CommentDaoJpa.class)
class CommentDaoJpaTest {

    @Autowired
    private CommentDao commentDao;
    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    void shouldReturnCommentForExistingId() {
        Optional<Comment> comment = commentDao.getById(1);
        assertThat(comment.get().getBook()).isEqualTo(testEntityManager.find(Book.class, 1L));
        assertThat(comment.get().getId()).isEqualTo(1L);
        assertThat(comment.get().getContent()).isEqualTo("The 10 Greatest Books of All Time");
    }

    @Test
    void shouldReturnNullIfCommentByIdNotExists() {
        Optional<Comment> comment = commentDao.getById(10);
        assertThat(comment).isEmpty();
    }

    @Test
    void shouldReturnCommentListForGetAllComments() {
        List<Comment> comments = commentDao.getAll();
        assertThat(comments).hasSize(3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewComment() {
        Comment commentToInsert = new Comment("Golden collection", testEntityManager.find(Book.class, 2L));
        commentDao.insert(commentToInsert);
        Long id = testEntityManager.getId(commentToInsert, Long.class);

        Comment insertedComment = testEntityManager.find(Comment.class, id);
        assertThat(insertedComment).isEqualTo(commentToInsert);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingComment() {
        Comment commentToUpdate = testEntityManager.find(Comment.class, 1L);
        testEntityManager.detach(commentToUpdate);
        commentToUpdate.setContent("The best of Russian classics");
        assertThat(testEntityManager.find(Comment.class, 1L)).isNotEqualTo(commentToUpdate);

        Comment updatedComment = commentDao.update(commentToUpdate);
        assertThat(updatedComment).isEqualTo(commentToUpdate);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingComment() {
        int modifiedRows = commentDao.deleteComment(1);
        assertThat(modifiedRows).isEqualTo(1);
        assertThat(testEntityManager.find(Comment.class, 1L)).isNull();
    }

    @Test
    void shouldReturn0IfNoCommentToDelete() {
        int modifiedRows = commentDao.deleteComment(10);
        assertThat(modifiedRows).isEqualTo(0);
    }

    @Test
    void shouldReturnCommentsByBookId() {
        List<Comment> commentsByBookId = commentDao.getCommentsByBookId(3L);
        assertThat(commentsByBookId).containsExactlyInAnyOrder(
                testEntityManager.find(Comment.class, 2L),
                testEntityManager.find(Comment.class, 3L));
    }
}
