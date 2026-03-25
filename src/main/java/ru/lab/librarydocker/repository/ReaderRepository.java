package ru.lab.librarydocker.repository;


import ru.lab.librarydocker.entity.Reader;

import java.util.List;
import java.util.Optional;

public interface ReaderRepository {
    List<Reader> findAll();
    Optional<Reader> findById(Long id);
    Reader save(Reader reader);
    void deleteById(Long id);
    boolean existsById(Long id);
}
