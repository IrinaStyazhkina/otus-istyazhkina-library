package ru.otus.istyazhkina.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.BookService;

import java.util.List;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Override
    @Transactional(readOnly = true)
    public long getBooksCount() {
        return bookDao.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookById(long id) throws DataOperationException {
        return bookDao.getById(id).orElseThrow(() -> new DataOperationException("Book by provided ID not found in database"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBooksByTitle(String name) {
        return bookDao.getByTitle(name);
    }

    @Override
    @Transactional
    public Book addNewBook(String bookTitle, String authorName, String authorSurname, String genreName) {
        if (authorDao.getByName(authorName, authorSurname).isEmpty()) {
            authorDao.insert(new Author(authorName, authorSurname));
        }
        if (genreDao.getByName(genreName).isEmpty()) {
            genreDao.insert(new Genre(genreName));
        }
        Book book = new Book(bookTitle, authorDao.getByName(authorName, authorSurname).get(), genreDao.getByName(genreName).get());
        bookDao.insert(book);
        return book;
    }

    @Override
    @Transactional
    public Book updateBookTitle(long id, String newTitle) throws DataOperationException {
        Book book = bookDao.getById(id).orElseThrow(() -> new DataOperationException("Book by provided ID not found in database"));
        book.setTitle(newTitle);
        bookDao.update(book);
        return book;
    }

    @Override
    @Transactional
    public int deleteBookById(long id) {
        return bookDao.deleteById(id);
    }
}
