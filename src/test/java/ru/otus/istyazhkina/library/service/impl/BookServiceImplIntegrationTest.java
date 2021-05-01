package ru.otus.istyazhkina.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.istyazhkina.library.dao.AuthorDao;
import ru.otus.istyazhkina.library.dao.BookDao;
import ru.otus.istyazhkina.library.dao.GenreDao;
import ru.otus.istyazhkina.library.domain.Book;
import ru.otus.istyazhkina.library.exceptions.NoEntityFoundInDataBaseException;
import ru.otus.istyazhkina.library.service.BookService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BookServiceImplIntegrationTest {

    @Autowired
    BookService bookService;

    @Autowired
    BookDao bookDao;

    @Autowired
    GenreDao genreDao;

    @Autowired
    AuthorDao authorDao;

    @Test
    void shouldAddAuthorAndGenreAndBookWhileInsertIfTheyNotExist() {
        assertThatThrownBy(() -> genreDao.getByName("play")).isInstanceOf(NoEntityFoundInDataBaseException.class);
        assertThatThrownBy(() -> authorDao.getByName("Anton", "Chekhov")).isInstanceOf(NoEntityFoundInDataBaseException.class);

        Book book = bookService.addNewBook("Seagull", "Anton", "Chekhov", "play");
        assertThat(genreDao.getByName("play")).isNotNull();
        assertThat(authorDao.getByName("Anton", "Chekhov")).isNotNull();
        assertThat(book.getTitle()).isEqualTo("Seagull");
        assertThat(book.getGenre().getName()).isEqualTo("play");
        assertThat(book.getAuthor().getName()).isEqualTo("Anton");
        assertThat(book.getAuthor().getSurname()).isEqualTo("Chekhov");
    }
}
