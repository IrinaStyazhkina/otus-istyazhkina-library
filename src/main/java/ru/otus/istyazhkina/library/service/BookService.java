package ru.otus.istyazhkina.library.service;

import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;

import java.util.List;

public interface BookService {

    long getBooksCount();

    List<Book> getAllBooks();

    Book getBookById(long id) throws NoEntityFoundInDataBaseException;

    Book getBookByName(String name) throws NoEntityFoundInDataBaseException;

    Book addNewBook(String bookTitle, String authorName, String authorSurname, String genreName);

    Book updateBookTitle(long id, String newTitle) throws NoEntityFoundInDataBaseException;

    int deleteBookById(long id);
}
