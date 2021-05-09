package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    void insert(Author author);

    Optional<Author> getById(long id);

    List<Author> getAll();

    Author update(Author author);

    int deleteAuthor(long id);

    Optional<Author> getByName(String name, String surname);
}
