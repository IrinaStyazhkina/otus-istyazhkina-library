package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.util.List;

public interface AuthorDao {

    long insert(Author author) throws DuplicateDataException;

    Author getById(long id);

    List<Author> getAll();

    int update(Author author) throws DuplicateDataException;

    int deleteById(long id) throws ConstraintException;

    Author getByName(String name, String surname);
}
