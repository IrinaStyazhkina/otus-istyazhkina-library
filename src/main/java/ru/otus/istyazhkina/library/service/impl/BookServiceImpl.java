package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
import ru.otus.istyazhkina.library.service.AuthorService;
import ru.otus.istyazhkina.library.service.BookService;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Override
    public long getBooksCount() {
        return bookDao.count();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

    @Override
    public Book getBookById(long id) {
        return bookDao.getById(id);
    }

    @Override
    public Book getBookByName(String name) {
        return bookDao.getByTitle(name);
    }

    @Override
    public Book addNewBook(String bookTitle, String authorName, String authorSurname, String genreName) throws DuplicateDataException {
        if (authorService.getAuthorByName(authorName, authorSurname) == null) {
            authorService.addNewAuthor(authorName, authorSurname);
        }

        if (genreService.getGenreByName(genreName) == null) {
            genreService.addNewGenre(genreName);
        }

        Author author = authorService.getAuthorByName(authorName, authorSurname);
        Genre genre = genreService.getGenreByName(genreName);
        Book book = new Book(bookTitle, author, genre);
        bookDao.insert(book);
        return getBookByName(bookTitle);
    }

    @Override
    public Book updateBookTitle(long id, String newTitle) throws NoDataException, DuplicateDataException {
        Book bookToUpdate = bookDao.getById(id);
        if (bookToUpdate == null) {
            throw new NoDataException("Can not update book's title because book with this id is not found");
        }
        bookToUpdate.setTitle(newTitle);
        bookDao.update(bookToUpdate);
        return bookDao.getById(id);
    }

    @Override
    public int deleteBookById(long id) {
        return bookDao.deleteById(id);
    }
}
