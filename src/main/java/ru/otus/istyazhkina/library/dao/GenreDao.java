package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;

import java.util.List;

public interface GenreDao {

    int insert(Genre genre) throws DuplicateDataException;

    int update(Genre genre) throws DuplicateDataException;

    Genre getById(long id);

    List<Genre> getAll();

    int deleteById(long id) throws ConstraintException;

    Genre getByName(String name);
}
