package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    public List<Genre> getAllGenres() {
        return genreDao.getAll();
    }

    @Override
    public Genre getGenreById(long id) {
        return genreDao.getById(id);
    }

    @Override
    public Genre getGenreByName(String name) {
        return genreDao.getByName(name);
    }

    @Override
    public Genre addNewGenre(String name) throws DuplicateDataException {
        genreDao.insert(new Genre(name));
        return getGenreByName(name);
    }

    @Override
    public Genre updateGenresName(long id, String newName) throws NoDataException, DuplicateDataException {
        Genre genreToUpdate = genreDao.getById(id);
        if (genreToUpdate == null) {
            throw new NoDataException("Can not update genre's name because genre with this id is not found");
        }
        genreToUpdate.setName(newName);
        genreDao.update(genreToUpdate);
        return genreDao.getById(id);
    }

    @Override
    public int deleteGenre(long id) throws ConstraintException {
        return genreDao.deleteById(id);
    }
}
