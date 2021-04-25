package ru.otus.istyazhkina.library.shell;

import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.istyazhkina.library.domain.Genre;
import ru.otus.istyazhkina.library.exceptions.ConstraintException;
import ru.otus.istyazhkina.library.exceptions.DuplicateDataException;
import ru.otus.istyazhkina.library.exceptions.NoDataException;
import ru.otus.istyazhkina.library.service.GenreService;

import java.util.List;

@ShellComponent
@AllArgsConstructor
public class GenreCommands {

    private final GenreService genreService;

    @ShellMethod(value = "Get all genres from table", key = {"all genres"})
    public String getAllGenres() {
        List<Genre> allGenres = genreService.getAllGenres();
        if (allGenres.size() == 0) {
            return "No data in table 'Genres'";
        }
        StringBuilder sb = new StringBuilder();
        for (Genre genre : allGenres) {
            sb.append(String.format("%s\t|\t%s \n", genre.getId(), genre.getName()));
        }
        return sb.toString();
    }

    @ShellMethod(value = "Get genre by ID", key = {"genre by id"})
    public String getGenreById(@ShellOption long id) {
        Genre genre = genreService.getGenreById(id);
        if (genre == null) return String.format("Genre with id %s is not found", id);
        return genre.getName();
    }

    @ShellMethod(value = "Update name of a genre by its ID", key = {"update genre"})
    public String updateGenreName(@ShellOption long id, @ShellOption String newName) {
        try {
            Genre genre = genreService.updateGenresName(id, newName);
            return String.format("Genre with id %s successfully updated. Genre name is %s", genre.getId(), genre.getName());
        } catch (NoDataException | DuplicateDataException e) {
            return e.getMessage();
        }

    }

    @ShellMethod(value = "Add new genre", key = {"add genre"})
    public String addNewGenre(@ShellOption String name) {
        try {
            genreService.addNewGenre(name);
            return String.format("Genre with name %s successfully added!", name);
        } catch (DuplicateDataException e) {
            return e.getMessage();
        }
    }

    @ShellMethod(value = "Get genre's ID by its name", key = {"genre by name"})
    public String getGenresId(@ShellOption String name) {
        Genre genreByName = genreService.getGenreByName(name);
        if (genreByName == null) return String.format("Genre with name %s is not found", name);
        return String.format("Genre's id is %s", genreByName.getId());
    }

    @ShellMethod(value = "Delete genre by ID", key = {"delete genre"})
    public String deleteGenreById(@ShellOption long id) {
        try {
            if (genreService.deleteGenre(id) == 1) {
                return "Genre is successfully deleted!";
            } else return "Sorry! We can not execute this operation!";
        } catch (ConstraintException e) {
            return e.getMessage();
        }
    }
}
