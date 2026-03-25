package ru.lab.librarydocker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.lab.librarydocker.dto.request.BookCreateRequest;
import ru.lab.librarydocker.dto.request.BookUpdateRequest;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Управление книгами")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех книг")
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить книгу по ID")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую книгу")
    public BookResponse createBook(@Valid @RequestBody BookCreateRequest request) {
        return bookService.createBook(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующую книгу")
    public BookResponse updateBook(@PathVariable Long id, @Valid @RequestBody BookUpdateRequest request) {
        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить книгу")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск книг по автору и/или жанру")
    public List<BookResponse> searchBooks(
            @Parameter(description = "ID автора") @RequestParam(required = false) Long authorId,
            @Parameter(description = "Жанр") @RequestParam(required = false) String genre) {
        if (authorId != null && genre != null) {
            return bookService.findBooksByAuthorAndGenre(authorId, genre);
        } else if (authorId != null) {
            return bookService.findBooksByAuthor(authorId);
        } else if (genre != null) {
            return bookService.findBooksByGenre(genre);
        } else {
            return bookService.getAllBooks();
        }
    }
}
