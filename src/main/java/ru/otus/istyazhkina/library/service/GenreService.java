package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;

import java.util.List;

public interface GenreService {

    List<Genre> getAllGenres();

    Genre getGenreById(long id) throws DataOperationException;

    Genre getGenreByName(String name) throws DataOperationException;

    Genre addNewGenre(String name) throws DataOperationException;

    Genre updateGenresName(long id, String newName) throws DataOperationException;

    int deleteGenre(long id) throws DataOperationException;
}
