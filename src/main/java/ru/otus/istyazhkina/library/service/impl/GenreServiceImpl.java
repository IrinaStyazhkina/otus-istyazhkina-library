package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.exceptions.ProhibitedDeletionException;
import ru.otus.istyazhkina.library.exceptions.SameEntityAlreadyExistsException;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(long id) throws DataOperationException {
        return genreDao.getById(id).orElseThrow(() -> new DataOperationException("No genre found by provided id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreByName(String name) throws DataOperationException {
        return genreDao.getByName(name).orElseThrow(() -> new DataOperationException("No genre found by provided name"));
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Genre addNewGenre(String name) throws DataOperationException {
        try {
            Genre genre = new Genre(name);
            genreDao.insert(genre);
            return genre;
        } catch (SameEntityAlreadyExistsException e) {
            throw new DataOperationException(e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Genre updateGenresName(long id, String newName) throws DataOperationException {
        Genre genre = genreDao.getById(id).orElseThrow(() -> new DataOperationException("Can not update genre. Genre by provided ID not found in database."));
        genre.setName(newName);
        try {
            genreDao.update(genre);
        } catch (SameEntityAlreadyExistsException e) {
            throw new DataOperationException(e.getMessage(), e);
        }
        return genre;
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public int deleteGenre(long id) throws DataOperationException {
        try {
            return genreDao.deleteGenre(id);
        } catch (ProhibitedDeletionException e) {
            throw new DataOperationException(e.getMessage(), e);
        }
    }
}
