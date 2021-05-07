package ru.otus.istyazhkina.library.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.istyazhkina.library.domain.Author;
import ru.otus.istyazhkina.library.exceptions.DataOperationException;
import ru.otus.istyazhkina.library.service.AuthorService;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class AuthorCommands {

    private final AuthorService authorService;

    @ShellMethod(value = "Get all authors from table", key = {"all authors"})
    public String getAllAuthors() {
        List<Author> allAuthors = authorService.getAllAuthors();
        if (allAuthors.size() == 0) {
            return "No data in table 'Authors'";
        }
        StringBuilder sb = new StringBuilder();
        for (Author author : allAuthors) {
            sb.append(String.format("%s\t|\t%s\t|\t%s\n", author.getId(), author.getName(), author.getSurname()));
        }
        return sb.toString();
    }

    @ShellMethod(value = "Get author by ID", key = {"author by id"})
    public String getAuthorById(@ShellOption long id) {
        try {
            Author author = authorService.getAuthorById(id);
            return String.format("%s %s", author.getName(), author.getSurname());
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Get author's ID by his name and surname", key = {"author by name"})
    public String getAuthorsId(@ShellOption String name, String surname) {
        try {
            Author authorByName = authorService.getAuthorByName(name, surname);
            return String.format("Author's id is %s", authorByName.getId());
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Add new author", key = {"add author"})
    public String addNewAuthor(@ShellOption String name, String surname) {
        try {
            authorService.addNewAuthor(name, surname);
            return String.format("Author with name %s %s successfully added!", name, surname);
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Update name of an author by his ID", key = {"update author"})
    public String updateAuthor(@ShellOption long id, @ShellOption String newName, @ShellOption String newSurname) {
        try {
            Author author = authorService.updateAuthor(id, newName, newSurname);
            return String.format("Author with id %s successfully updated. Author's name is %s %s", author.getId(), author.getName(), author.getSurname());
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Delete author by ID", key = {"delete author"})
    public String deleteAuthorById(@ShellOption long id) {
        try {
            if (authorService.deleteAuthor(id) == 1) {
                return "Author is successfully deleted!";
            }
            return "Deletion is not successful. Please check if provided author id exists";
        } catch (DataOperationException e) {
            return e.getMessage();
        }
    }
}
