package ru.lab.librarydocker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.lab.librarydocker.dto.request.AuthorCreateRequest;
import ru.lab.librarydocker.dto.request.AuthorUpdateRequest;
import ru.lab.librarydocker.dto.response.AuthorResponse;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.service.AuthorService;
import ru.lab.librarydocker.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "Управление авторами")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех авторов")
    public List<AuthorResponse> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить автора по ID")
    public AuthorResponse getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать нового автора")
    public AuthorResponse createAuthor(@Valid @RequestBody AuthorCreateRequest request) {
        return authorService.createAuthor(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные автора")
    public AuthorResponse updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorUpdateRequest request) {
        return authorService.updateAuthor(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить автора")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Получить все книги указанного автора")
    public List<BookResponse> getBooksByAuthor(@PathVariable Long id) {
        return bookService.findBooksByAuthor(id);
    }
}
