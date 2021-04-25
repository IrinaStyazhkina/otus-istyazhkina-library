package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(long id);

    Genre getGenreByName(String name);

    Genre addNewGenre(String name) throws DuplicateDataException;

    Genre updateGenresName(long id, String newName) throws NoDataException, DuplicateDataException;

    int deleteGenre(long id) throws ConstraintException;
}
