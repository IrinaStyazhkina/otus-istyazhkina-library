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
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
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
    public Book getBookById(long id) throws NoEntityFoundInDataBaseException {
        return bookDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Book by provided ID not found in database"));
    }

    @Override
    @Transactional(readOnly = true)
    public Book getBookByName(String name) {
        return bookDao.getByTitle(name);
    }

    @Override
    @Transactional
    public Book addNewBook(String bookTitle, String authorName, String authorSurname, String genreName) {
        Author author;
        Genre genre;
        try {
            author = authorDao.getByName(authorName, authorSurname);
        } catch (NoEntityFoundInDataBaseException e) {
            authorDao.insert(new Author(authorName, authorSurname));
            author = authorDao.getByName(authorName, authorSurname);
        }

        try {
            genre = genreDao.getByName(genreName);
        } catch (NoEntityFoundInDataBaseException e) {
            genreDao.insert(new Genre(genreName));
            genre = genreDao.getByName(genreName);
        }
        Book book = new Book(bookTitle, author, genre);
        bookDao.insert(book);
        return bookDao.getByTitle(bookTitle);
    }


    @Override
    @Transactional
    public Book updateBookTitle(long id, String newTitle) throws NoEntityFoundInDataBaseException {
        Book bookToUpdate = bookDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Book by provided ID not found in database"));
        bookToUpdate.setTitle(newTitle);
        bookDao.update(bookToUpdate);
        return bookDao.getById(id).orElseThrow(() -> new NoEntityFoundInDataBaseException("Book by provided ID not found in database"));
    }

    @Override
    @Transactional
    public int deleteBookById(long id) {
        return bookDao.deleteById(id);
    }
}
