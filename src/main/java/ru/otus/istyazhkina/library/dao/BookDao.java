package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    long count();

    void insert(Book book);

    Book update(Book book);

    Optional<Book> getById(long id);

    Book getByTitle(String title) throws NoEntityFoundInDataBaseException;

    List<Book> getAll();

    int deleteById(long id);
}
