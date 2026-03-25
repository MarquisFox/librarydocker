package ru.lab.librarydocker.entity;

public class Book {
    private Long id;
    private String title;
    private Long authorId;
    private String genre;
    private Integer publishedYear;
    private String isbn;
    private Integer availableCopies;

    public Book() {}

    public Book(String title, Long authorId, String genre, Integer publishedYear, String isbn, Integer availableCopies) {
        this.title = title;
        this.authorId = authorId;
        this.genre = genre;
        this.publishedYear = publishedYear;
        this.isbn = isbn;
        this.availableCopies = availableCopies;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }
}
