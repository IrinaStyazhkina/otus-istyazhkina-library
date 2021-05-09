package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;

import java.util.List;

public interface BookService {

    long getBooksCount();

    List<Book> getAllBooks();

    Book getBookById(long id) throws DataOperationException;

    List<Book> getBooksByTitle(String name);

    Book addNewBook(String bookTitle, String authorName, String authorSurname, String genreName);

    Book updateBookTitle(long id, String newTitle) throws DataOperationException;

    int deleteBookById(long id);
}
