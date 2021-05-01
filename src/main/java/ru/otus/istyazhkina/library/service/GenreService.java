package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(long id) throws NoEntityFoundInDataBaseException;

    Genre getGenreByName(String name) throws NoEntityFoundInDataBaseException;

    Genre addNewGenre(String name) throws SameEntityAlreadyExistsException;

    Genre updateGenresName(long id, String newName) throws NoEntityFoundInDataBaseException, SameEntityAlreadyExistsException;

    int deleteGenre(long id) throws ProhibitedDeletionException;
}
