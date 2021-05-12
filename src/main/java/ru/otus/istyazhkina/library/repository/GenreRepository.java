package ru.otus.istyazhkina.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.istyazhkina.library.domain.Genre;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByName(String name);

}
