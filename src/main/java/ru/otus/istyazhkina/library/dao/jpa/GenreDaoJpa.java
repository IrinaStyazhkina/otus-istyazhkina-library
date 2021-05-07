package ru.otus.istyazhkina.library.dao.jpa;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class GenreDaoJpa implements GenreDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insert(Genre genre) {
        try {
            em.persist(genre);
        } catch (PersistenceException e) {
            throw e.getCause() instanceof ConstraintViolationException
                    ? new SameEntityAlreadyExistsException("Genre with this name already exists in database")
                    : e;
        }
    }

    @Override
    public Genre update(Genre genre) {
        try {
            Genre mergedGenre = em.merge(genre);
            em.flush();
            return mergedGenre;
        } catch (PersistenceException e) {
            throw e.getCause() instanceof ConstraintViolationException
                    ? new SameEntityAlreadyExistsException("Can not update genre because genre with provided name already exists in database")
                    : e;
        }
    }

    @Override
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> getAll() {
        return em.createQuery("select a from Genre a", Genre.class).getResultList();
    }

    @Override
    public int deleteGenre(long id) {
        try {
            return em.createQuery("delete from Genre a where a.id = :id").setParameter("id", id).executeUpdate();
        } catch (PersistenceException e) {
            throw e.getCause() instanceof ConstraintViolationException
                    ? new ProhibitedDeletionException("This operation is not allowed! In system exists book with this genre")
                    : e;
        }
    }

    @Override
    public Optional<Genre> getByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select a from Genre a where a.name=:name", Genre.class).setParameter("name", name);
        try {
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
