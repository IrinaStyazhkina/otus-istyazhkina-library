package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;

import java.util.List;

public interface BookService {

    long getBooksCount();

    List<Book> getAllBooks();

    Book getBookById(long id);

    Book getBookByName(String name);

    Book addNewBook(String bookTitle, String authorName, String authorSurname, String genreName) throws DuplicateDataException;

    Book updateBookTitle(long id, String newTitle) throws NoDataException, DuplicateDataException;

    int deleteBookById(long id);
}
