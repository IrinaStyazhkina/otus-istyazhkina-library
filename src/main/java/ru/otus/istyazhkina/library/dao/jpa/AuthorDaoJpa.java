package ru.otus.istyazhkina.library.dao.jpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.domain.Author;
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

@Repository
@AllArgsConstructor
public class AuthorDaoJpa implements AuthorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void insert(Author author) throws SameEntityAlreadyExistsException {
        try {
            em.persist(author);
        } catch (PersistenceException e) {
            throw new SameEntityAlreadyExistsException("Author with this name and surname already exists in database");
        }
    }

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select a from Author a", Author.class).getResultList();
    }

    @Override
    public Author update(Author author) throws SameEntityAlreadyExistsException {
        try {
            Author mergedAuthor = em.merge(author);
            em.flush();
            return mergedAuthor;
        } catch (PersistenceException e) {
            throw new SameEntityAlreadyExistsException("Can not update author because author with provided name and surname already exists in database");
        }
    }

    @Override
    public int deleteAuthor(long id) throws ProhibitedDeletionException {
        try {
            return em.createQuery("delete from Author a where a.id = :id").setParameter("id", id).executeUpdate();
        } catch (PersistenceException e) {
            throw new ProhibitedDeletionException("This operation is not allowed! In system exists book with this author");
        }
    }

    @Override
    public Author getByName(String name, String surname) throws NoEntityFoundInDataBaseException {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name=:name and a.surname=:surname", Author.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new NoEntityFoundInDataBaseException("No Author found by name " + name + " " + surname);
        }
    }
}
