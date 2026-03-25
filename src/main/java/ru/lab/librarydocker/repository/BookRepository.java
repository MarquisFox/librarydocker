package ru.lab.librarydocker.repository;

import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    void deleteById(Long id);

    boolean existsById(Long id);


    List<BookResponse> findAllWithAuthorNames();

    Optional<BookResponse> findByIdWithAuthorName(Long id);

    List<BookResponse> findByAuthorIdWithAuthorName(Long authorId);

    List<BookResponse> findByGenreWithAuthorName(String genre);

    List<BookResponse> findByAuthorIdAndGenreWithAuthorName(Long authorId, String genre);
}
