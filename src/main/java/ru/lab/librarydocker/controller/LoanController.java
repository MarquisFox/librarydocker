package ru.lab.librarydocker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.lab.librarydocker.dto.request.LoanCreateRequest;
import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@Tag(name = "Loans", description = "Управление выдачей книг")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех выдач")
    public List<LoanResponse> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить выдачу по ID")
    public LoanResponse getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Выдать книгу читателю")
    public LoanResponse issueBook(@Valid @RequestBody LoanCreateRequest request) {
        return loanService.issueBook(request);
    }

    @PutMapping("/{id}/return")
    @Operation(summary = "Отметить книгу как возвращённую")
    public LoanResponse returnBook(@PathVariable Long id) {
        return loanService.returnBook(id);
    }

    @GetMapping("/reader/{readerId}")
    @Operation(summary = "Получить все выдачи конкретного читателя")
    public List<LoanResponse> getLoansByReader(@PathVariable Long readerId) {
        return loanService.getLoansByReader(readerId);
    }

    @GetMapping("/book/{bookId}")
    @Operation(summary = "Получить историю выдач конкретной книги")
    public List<LoanResponse> getLoansByBook(@PathVariable Long bookId) {
        return loanService.getLoansByBook(bookId);
    }
}
