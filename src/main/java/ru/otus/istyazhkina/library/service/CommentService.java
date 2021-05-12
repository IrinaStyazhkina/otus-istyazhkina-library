package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments();

    Comment getCommentById(long id) throws DataOperationException;

    Comment addNewComment(String name, long bookId) throws DataOperationException;

    Comment updateCommentContent(long id, String newContent) throws DataOperationException;

    void deleteComment(long id) throws DataOperationException;

    List<Comment> getCommentsByBookId(long bookId);
}
