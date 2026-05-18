package ru.lab.librarydocker.dto.request;

import jakarta.validation.constraints.*;

public class BookCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    private String genre;

    @Min(1000)
    @Max(2025)
    private Integer publishedYear;

    @Pattern(regexp = "^(97[8-9]|97[8-9]?[0-9]{10}|[0-9]{9,13})$",
            message = "Invalid ISBN format")
    private String isbn;

    @Min(0)
    private Integer availableCopies;

    public @NotBlank(message = "Title is required") String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "Title is required") String title) {
        this.title = title;
    }

    public @NotNull(message = "Author ID is required") Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(@NotNull(message = "Author ID is required") Long authorId) {
        this.authorId = authorId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public @Min(1000) @Max(2025) Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(@Min(1000) @Max(2025) Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public @Pattern(regexp = "^(97[8-9]|97[8-9]?[0-9]{10}|[0-9]{9,13})$",
            message = "Invalid ISBN format") String getIsbn() {
        return isbn;
    }

    public void setIsbn(@Pattern(regexp = "^(97[8-9]|97[8-9]?[0-9]{10}|[0-9]{9,13})$",
            message = "Invalid ISBN format") String isbn) {
        this.isbn = isbn;
    }

    public @Min(0) Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(@Min(0) Integer availableCopies) {
        this.availableCopies = availableCopies;
    }
}

