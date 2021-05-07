package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    void insert(Genre genre);

    Genre update(Genre genre);

    Optional<Genre> getById(long id);

    List<Genre> getAll();

    int deleteGenre(long id);

    Optional<Genre> getByName(String name);
}
