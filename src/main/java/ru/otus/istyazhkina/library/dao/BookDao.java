package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    long count();

    void insert(Book book);

    Book update(Book book);

    Optional<Book> getById(long id);

    List<Book> getByTitle(String title);

    List<Book> getAll();

    int deleteById(long id);
}
