package ru.otus.istyazhkina.library.dao.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.CommentDao;
import ru.otus.istyazhkina.library.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CommentDaoJpa implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insert(Comment comment) {
        em.persist(comment);
    }

    @Override
    public Comment update(Comment comment) {
        Comment mergedComment = em.merge(comment);
        em.flush();
        return mergedComment;
    }

    @Override
    public Optional<Comment> getById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> getAll() {
        return em.createQuery("select a from Comment a join fetch a.book", Comment.class).getResultList();
    }

    @Override
    public int deleteComment(long id) {
        return em.createQuery("delete from Comment a where a.id = :id").setParameter("id", id).executeUpdate();
    }

    @Override
    public List<Comment> getCommentsByBookId(long bookId) {
        return em.createQuery("select a.comments from Book a where a.id=:id").setParameter("id", bookId).getResultList();
    }
}
