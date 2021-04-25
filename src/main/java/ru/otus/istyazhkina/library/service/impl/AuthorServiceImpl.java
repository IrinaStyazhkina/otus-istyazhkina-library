package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.getAll();
    }

    @Override
    public Author getAuthorById(long id) {
        return authorDao.getById(id);
    }

    @Override
    public Author getAuthorByName(String name, String surname) {
        return authorDao.getByName(name, surname);
    }

    @Override
    public Author addNewAuthor(String name, String surname) throws DuplicateDataException {
        authorDao.insert(new Author(name, surname));
        return getAuthorByName(name, surname);
    }

    @Override
    public Author updateAuthorsName(long id, String newName) throws NoDataException, DuplicateDataException {
        Author author = getAuthorById(id);
        if (author == null) {
            throw new NoDataException("Can not update author's name because author with this id is not found");
        }
        author.setName(newName);
        authorDao.update(author);
        return authorDao.getById(id);
    }

    @Override
    public Author updateAuthorsSurname(long id, String newSurname) throws NoDataException, DuplicateDataException {
        Author author = getAuthorById(id);
        if (author == null) {
            throw new NoDataException("Can not update author's surname because author with this id is not found");
        }
        author.setSurname(newSurname);
        authorDao.update(author);
        return authorDao.getById(id);
    }

    @Override
    public int deleteAuthor(long id) throws ConstraintException {
        return authorDao.deleteById(id);
    }
}
