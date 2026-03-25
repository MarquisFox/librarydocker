package ru.lab.librarydocker.service;
import ru.lab.librarydocker.dto.request.BookCreateRequest;
import ru.lab.librarydocker.dto.request.BookUpdateRequest;
import ru.lab.librarydocker.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> getAllBooks();
    BookResponse getBookById(Long id);
    BookResponse createBook(BookCreateRequest request);
    BookResponse updateBook(Long id, BookUpdateRequest request);
    void deleteBook(Long id);
    List<BookResponse> findBooksByAuthor(Long authorId);
    List<BookResponse> findBooksByGenre(String genre);
    List<BookResponse> findBooksByAuthorAndGenre(Long authorId, String genre);
}
