package ru.otus.istyazhkina.library.dao.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class BookDaoJpa implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return em.createQuery("select count(b)from Book b", Long.class).getSingleResult();
    }

    @Override
    public void insert(Book book) {
        em.persist(book);
    }

    @Override
    public Book update(Book book) {
        Book mergedBook = em.merge(book);
        em.flush();
        return mergedBook;
    }

    @Override
    public Optional<Book> getById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Book getByTitle(String title) throws NoEntityFoundInDataBaseException {
        TypedQuery<Book> query = em.createQuery("select a from Book a where a.title=:title", Book.class).setParameter("title", title);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoEntityFoundInDataBaseException("No Book found by title " + title);
        }
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select distinct a from Book a " +
                "left join fetch a.author " +
                "left join fetch a.genre " +
                "left join fetch a.comments", Book.class).getResultList();
    }

    @Override
    public int deleteById(long id) {
        return em.createQuery("delete from Book a where a.id = :id").setParameter("id", id).executeUpdate();
    }
}
