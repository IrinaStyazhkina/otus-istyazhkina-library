package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    void insert(Genre genre) throws SameEntityAlreadyExistsException;

    Genre update(Genre genre) throws SameEntityAlreadyExistsException;

    Optional<Genre> getById(long id);

    List<Genre> getAll();

    int deleteGenre(long id) throws ProhibitedDeletionException;

    Genre getByName(String name) throws NoEntityFoundInDataBaseException;
}
