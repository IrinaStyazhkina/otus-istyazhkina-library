package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    void insert(Author author) throws SameEntityAlreadyExistsException;

    Optional<Author> getById(long id);

    List<Author> getAll();

    Author update(Author author) throws SameEntityAlreadyExistsException;

    int deleteAuthor(long id) throws ProhibitedDeletionException;

    Author getByName(String name, String surname) throws NoEntityFoundInDataBaseException;
}
