package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAllAuthors() {
        return authorDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorById(long id) throws DataOperationException {
        return authorDao.getById(id).orElseThrow(() -> new DataOperationException("Author by provided ID not found in database"));
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorByName(String name, String surname) throws DataOperationException {
        return authorDao.getByName(name, surname).orElseThrow(() -> new DataOperationException("No author found by provided name"));
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Author addNewAuthor(String name, String surname) throws DataOperationException {
        try {
            Author author = new Author(name, surname);
            authorDao.insert(author);
            return author;
        } catch (SameEntityAlreadyExistsException e) {
            throw new DataOperationException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Author updateAuthor(long id, String newName, String newSurname) throws DataOperationException {
        Author author = authorDao.getById(id).orElseThrow(() -> new DataOperationException("Can not update author. Author by provided ID not found in database."));
        author.setName(newName);
        author.setSurname(newSurname);
        try {
            authorDao.update(author);
        } catch (SameEntityAlreadyExistsException e) {
            throw new DataOperationException(e.getMessage(), e);
        }
        return author;
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public int deleteAuthor(long id) throws DataOperationException {
        try {
            return authorDao.deleteAuthor(id);
        } catch (ProhibitedDeletionException e) {
            throw new DataOperationException(e.getMessage(), e);
        }
    }
}
