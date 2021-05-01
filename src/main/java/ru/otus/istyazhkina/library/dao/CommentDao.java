package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {

    void insert(Comment comment);

    Comment update(Comment comment);

    Optional<Comment> getById(long id);

    List<Comment> getAll();

    int deleteComment(long id);

    List<Comment> getCommentsByBookId(long bookId);
}
