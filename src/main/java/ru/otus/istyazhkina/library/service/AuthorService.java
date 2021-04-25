package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(long id);

    Author getAuthorByName(String name, String surname);

    Author addNewAuthor(String name, String surname) throws DuplicateDataException;

    Author updateAuthorsName(long id, String newName) throws NoDataException, DuplicateDataException;

    Author updateAuthorsSurname(long id, String newSurname) throws NoDataException, DuplicateDataException;

    int deleteAuthor(long id) throws ConstraintException;
}
