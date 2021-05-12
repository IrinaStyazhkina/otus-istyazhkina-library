package ru.otus.istyazhkina.library.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.istyazhkina.library.domain.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitle(String title);

    @EntityGraph(attributePaths = {"author", "genre"})
    List<Book> findAll();

}
