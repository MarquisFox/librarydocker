package ru.lab.librarydocker.repository;


import ru.lab.librarydocker.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    List<Author> findAll();
    Optional<Author> findById(Long id);
    Author save(Author author);
    void deleteById(Long id);
    boolean existsById(Long id);
}
