package ru.lab.librarydocker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.lab.librarydocker.dto.request.ReaderCreateRequest;
import ru.lab.librarydocker.dto.request.ReaderUpdateRequest;
import ru.lab.librarydocker.dto.response.ReaderResponse;
import ru.lab.librarydocker.service.ReaderService;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
@Tag(name = "Readers", description = "Управление читателями")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    @Operation(summary = "Получить список всех читателей")
    public List<ReaderResponse> getAllReaders() {
        return readerService.getAllReaders();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить читателя по ID")
    public ReaderResponse getReaderById(@PathVariable Long id) {
        return readerService.getReaderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Зарегистрировать нового читателя")
    public ReaderResponse createReader(@Valid @RequestBody ReaderCreateRequest request) {
        return readerService.createReader(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные читателя")
    public ReaderResponse updateReader(@PathVariable Long id, @Valid @RequestBody ReaderUpdateRequest request) {
        return readerService.updateReader(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить читателя")
    public void deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
    }
}
