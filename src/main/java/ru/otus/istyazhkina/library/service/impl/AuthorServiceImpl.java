package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
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
    public Author getAuthorById(long id) throws NoEntityFoundInDataBaseException {
        return authorDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Author by provided ID not found in database"));
    }

    @Override
    @Transactional(readOnly = true)
    public Author getAuthorByName(String name, String surname) throws NoEntityFoundInDataBaseException {
        return authorDao.getByName(name, surname);
    }

    @Override
    @Transactional
    public Author addNewAuthor(String name, String surname) throws SameEntityAlreadyExistsException {
        authorDao.insert(new Author(name, surname));
        return authorDao.getByName(name, surname);
    }

    @Override
    @Transactional
    public Author updateAuthor(long id, String newName, String newSurname) throws NoEntityFoundInDataBaseException, SameEntityAlreadyExistsException {
        Author author = authorDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Can not update author. Author by provided ID not found in database."));
        author.setName(newName);
        author.setSurname(newSurname);
        authorDao.update(author);
        return authorDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Unexpected error while getting data from database"));
    }

    @Override
    @Transactional
    public int deleteAuthor(long id) throws ProhibitedDeletionException, NoEntityFoundInDataBaseException {
        return authorDao.deleteAuthor(id);
    }
}
