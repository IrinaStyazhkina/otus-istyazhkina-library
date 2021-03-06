package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;

import java.util.List;

public interface AuthorService {

    List<Author> getAllAuthors();

    Author getAuthorById(long id) throws DataOperationException;

    Author getAuthorByName(String name, String surname) throws DataOperationException;

    Author addNewAuthor(String name, String surname) throws DataOperationException;

    Author updateAuthor(long id, String newName, String newSurname) throws DataOperationException;

    void deleteAuthor(long id) throws DataOperationException;
}
