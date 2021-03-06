package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.repository.GenreRepository;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreById(long id) throws DataOperationException {
        return genreRepository.findById(id).orElseThrow(() -> new DataOperationException("No genre found by provided id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getGenreByName(String name) throws DataOperationException {
        return genreRepository.findByName(name).orElseThrow(() -> new DataOperationException("No genre found by provided name"));
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Genre addNewGenre(String name) throws DataOperationException {
        try {
            Genre genre = new Genre(name);
            return genreRepository.save(genre);
        } catch (DataIntegrityViolationException e) {
            throw new DataOperationException("Can not add genre because genre already exists!");
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public Genre updateGenresName(long id, String newName) throws DataOperationException {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new DataOperationException("Can not update genre. Genre by provided ID not found"));
        genre.setName(newName);
        try {
            return genreRepository.saveAndFlush(genre);
        } catch (DataIntegrityViolationException e) {
            throw new DataOperationException("Can not update genre because genre with same name already exists!");
        }
    }

    @Override
    @Transactional(rollbackFor = DataOperationException.class)
    public void deleteGenre(long id) throws DataOperationException {
        try {
            genreRepository.deleteById(id);
            genreRepository.flush();
        } catch (
                EmptyResultDataAccessException e) {
            throw new DataOperationException("There is no genre with provided id");
        } catch (DataIntegrityViolationException e) {
            throw new DataOperationException("You can not delete genre because exists book with this genre!");
        }
    }
}
