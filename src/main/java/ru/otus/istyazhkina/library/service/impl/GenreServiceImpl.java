package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
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
    public Genre getGenreById(long id) throws NoEntityFoundInDataBaseException {
        return genreDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Genre by provided ID not found in database"));
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreByName(String name) throws NoEntityFoundInDataBaseException {
        return genreDao.getByName(name);
    }

    @Override
    @Transactional
    public Genre addNewGenre(String name) throws SameEntityAlreadyExistsException {
        genreDao.insert(new Genre(name));
        return genreDao.getByName(name);
    }

    @Override
    @Transactional
    public Genre updateGenresName(long id, String newName) throws NoEntityFoundInDataBaseException, SameEntityAlreadyExistsException {
        Genre genreToUpdate = genreDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Can not update genre. Genre by provided ID not found in database."));
        genreToUpdate.setName(newName);
        genreDao.update(genreToUpdate);
        return genreDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Unexpected error while getting data from database"));
    }

    @Override
    @Transactional
    public int deleteGenre(long id) throws ProhibitedDeletionException {
        return genreDao.deleteGenre(id);
    }
}
