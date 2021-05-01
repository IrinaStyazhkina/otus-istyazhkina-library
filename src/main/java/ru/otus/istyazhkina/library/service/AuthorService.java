package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(long id) throws NoEntityFoundInDataBaseException;

    Author getAuthorByName(String name, String surname) throws NoEntityFoundInDataBaseException;

    Author addNewAuthor(String name, String surname) throws SameEntityAlreadyExistsException;

    Author updateAuthor(long id, String newName, String newSurname) throws NoEntityFoundInDataBaseException, SameEntityAlreadyExistsException;

    int deleteAuthor(long id) throws ProhibitedDeletionException, NoEntityFoundInDataBaseException;
}
