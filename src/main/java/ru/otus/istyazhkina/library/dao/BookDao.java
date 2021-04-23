package ru.otus.istyazhkina.library.dao;

import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;

import java.util.List;

public interface BookDao {

    int count();

    long insert(Book book) throws DuplicateDataException;

    int update(Book book) throws NoDataException, DuplicateDataException;

    Book getById(long id);

    Book getByTitle(String title);

    List<Book> getAll();

    int deleteById(long id);

}
