package ru.otus.istyazhkina.library.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.istyazhkina.library.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBookId(long id);

    @EntityGraph(attributePaths = {"book"})
    List<Comment> findAll();
}
