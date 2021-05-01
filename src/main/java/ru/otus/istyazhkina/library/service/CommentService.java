package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;

import java.util.List;

public interface CommentService {

    List<Comment> getAllComments();

    Comment getCommentById(long id) throws NoEntityFoundInDataBaseException;

    Comment addNewComment(String name, long bookId) throws NoEntityFoundInDataBaseException;

    Comment updateCommentContent(long id, String newContent) throws NoEntityFoundInDataBaseException;

    int deleteComment(long id);

    List<Comment> getCommentsByBookId(long bookId);
}
