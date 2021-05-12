package ru.otus.istyazhkina.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldReturnCommentForExistingId() {
        Optional<Comment> actualComment = commentRepository.findById(1L);
        Comment expectedComment = testEntityManager.find(Comment.class, 1L);
        assertThat(actualComment).isPresent().get().usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @Test
    void shouldReturnEmptyOptionalIfCommentByIdNotExists() {
        Optional<Comment> comment = commentRepository.findById(10L);
        assertThat(comment).isEmpty();
    }

    @Test
    void shouldReturnCommentListForGetAllComments() {
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments).hasSize(3);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertNewComment() {
        Book book = testEntityManager.find(Book.class, 2L);
        Comment newComment = new Comment("Golden collection", book);

        commentRepository.save(newComment);
        assertThat(newComment.getId()).isGreaterThan(0);

        Comment actualComment = testEntityManager.find(Comment.class, newComment.getId());
        assertThat(actualComment)
                .matches(c -> c.getContent().equals("Golden collection"))
                .matches(c -> c.getBook().equals(book));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldUpdateExistingComment() {
        Comment commentToUpdate = testEntityManager.find(Comment.class, 1L);
        String oldContent = commentToUpdate.getContent();
        String newContent = "The best of Russian classics";

        testEntityManager.detach(commentToUpdate);

        commentToUpdate.setContent(newContent);
        commentRepository.saveAndFlush(commentToUpdate);

        Comment updatedComment = testEntityManager.find(Comment.class, commentToUpdate.getId());
        assertThat(updatedComment.getContent()).isNotEqualTo(oldContent).isEqualTo(newContent);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldDeleteExistingComment() {
        Comment comment = testEntityManager.find(Comment.class, 1L);
        assertThat(comment).isNotNull();
        testEntityManager.detach(comment);

        commentRepository.deleteById(1L);
        assertThat(testEntityManager.find(Comment.class, 1L)).isNull();
    }

    @Test
    void shouldThrowExceptionIfNoCommentToDelete() {
        assertThatThrownBy(() -> {
            commentRepository.deleteById(10L);
            commentRepository.flush();
        }).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void shouldReturnCommentsByBookId() {
        List<Comment> commentsByBookId = commentRepository.findAllByBookId(3L);
        assertThat(commentsByBookId).containsExactlyInAnyOrder(
                testEntityManager.find(Comment.class, 2L),
                testEntityManager.find(Comment.class, 3L));
    }
}
