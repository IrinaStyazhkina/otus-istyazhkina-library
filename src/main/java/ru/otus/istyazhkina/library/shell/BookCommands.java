package ru.otus.istyazhkina.library.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.BookService;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class BookCommands {

    private final BookService bookService;

    @ShellMethod(value = "Get all books from table", key = {"all books"})
    public String getAllBooks() {
        List<Book> allBooks = bookService.getAllBooks();
        if (allBooks.size() == 0) {
            return "No data in table 'Books'";
        }
        StringBuilder sb = new StringBuilder();
        for (Book book : allBooks) {
            sb.append(String.format("%s\t|\t%s\t|\t%s\t|\t%s\n", book.getId(), book.getTitle(), book.getAuthor(), book.getGenre()));
        }
        return sb.toString();
    }

    @ShellMethod(value = "Get book by ID", key = {"book by id"})
    public String getBookById(@ShellOption long id) {
        try {
            Book book = bookService.getBookById(id);
            return String.format("%s", book.getTitle());
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Get book's ID by its title", key = {"book by title"})
    public String getBooksId(@ShellOption String title) {
        List<Book> booksByTitle = bookService.getBooksByTitle(title);
        if (booksByTitle.size() == 0) {
            return "No books found by provided title";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Id's of books with provided title:");
        for (Book book : booksByTitle) {
            sb.append("\n").append(book.getId());
        }
        return sb.toString();
    }

    @ShellMethod(value = "Add new book", key = {"add book"})
    public String addNewBook(@ShellOption String title, @ShellOption String authorName, @ShellOption String authorSurname, @ShellOption String genreName) {
        bookService.addNewBook(title, authorName, authorSurname, genreName);
        return String.format("Book with title %s successfully added!", title);
    }

    @ShellMethod(value = "Update title of a book by its ID", key = {"update book title"})
    public String updateBookTitle(@ShellOption long id, @ShellOption String newTitle) {
        try {
            Book book = bookService.updateBookTitle(id, newTitle);
            return String.format("Book with id %s is successfully updated. Book's title is %s ", book.getId(), book.getTitle());
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete book by ID", key = {"delete book"})
    public String deleteBookById(@ShellOption long id) {
        if (bookService.deleteBookById(id) == 1) {
            return "Book is successfully deleted!";
        } else return "Sorry! We can not execute this operation!";
    }

    @ShellMethod(value = "Get count of all books", key = {"books count"})
    public long getAllBooksCount() {
        return bookService.getBooksCount();
    }
}
