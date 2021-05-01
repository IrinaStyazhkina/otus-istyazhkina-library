package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.dao.CommentDao;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Comment;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
import ru.otus.istyazhkina.library.service.CommentService;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;
    private final BookDao bookDao;

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllComments() {
        return commentDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getCommentById(long id) throws NoEntityFoundInDataBaseException {
        return commentDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Comment by provided ID not found in database"));
    }

    @Override
    @Transactional
    public Comment addNewComment(String content, long bookId) throws NoEntityFoundInDataBaseException {
        Book book = bookDao.getById(bookId).orElseThrow(() -> new NoEntityFoundInDataBaseException("Can not add new Comment. Book by provided id is not found!"));
        Comment comment = new Comment(content, book);
        commentDao.insert(new Comment(content, book));
        return comment;
    }

    @Override
    @Transactional
    public Comment updateCommentContent(long id, String newContent) throws NoEntityFoundInDataBaseException {
        Comment commentToUpdate = commentDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Can not update comment. Comment by provided ID not found in database."));
        commentToUpdate.setContent(newContent);
        commentDao.update(commentToUpdate);
        return commentDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Unexpected error while getting data from database"));
    }

    @Override
    @Transactional
    public int deleteComment(long id) {
        return commentDao.deleteComment(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getCommentsByBookId(long bookId) {
        return commentDao.getCommentsByBookId(bookId);
    }
}
