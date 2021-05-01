package ru.otus.istyazhkina.library.dao.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
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
    public void insert(Genre genre) throws SameEntityAlreadyExistsException {
        try {
            em.persist(genre);
        } catch (PersistenceException e) {
            throw new SameEntityAlreadyExistsException("Genre with this name already exists in database");
        }
    }

    @Override
    public Genre update(Genre genre) throws SameEntityAlreadyExistsException {
        try {
            Genre mergedGenre = em.merge(genre);
            em.flush();
            return mergedGenre;
        } catch (PersistenceException e) {
            throw new SameEntityAlreadyExistsException("Can not update genre because genre with provided name already exists in database");
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
    public int deleteGenre(long id) throws ProhibitedDeletionException {
        try {
            return em.createQuery("delete from Genre a where a.id = :id").setParameter("id", id).executeUpdate();
        } catch (PersistenceException e) {
            throw new ProhibitedDeletionException("This operation is not allowed! In system exists book with this genre");
        }
    }

    @Override
    public Genre getByName(String name) throws NoEntityFoundInDataBaseException {
        TypedQuery<Genre> query = em.createQuery("select a from Genre a where a.name=:name", Genre.class).setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoEntityFoundInDataBaseException("No Genre found by name " + name);
        }
    }
}
