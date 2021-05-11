package ru.otus.istyazhkina.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.istyazhkina.library.domain.Author;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameAndSurname(String name, String surname);

}
