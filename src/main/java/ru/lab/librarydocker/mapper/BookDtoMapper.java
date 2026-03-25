package ru.lab.librarydocker.mapper;

import ru.lab.librarydocker.dto.request.BookCreateRequest;
import ru.lab.librarydocker.dto.request.BookUpdateRequest;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.entity.Book;

public final class BookDtoMapper {

    private BookDtoMapper() {}

    public static Book toEntity(BookCreateRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthorId(request.getAuthorId());
        book.setGenre(request.getGenre());
        book.setPublishedYear(request.getPublishedYear());
        book.setIsbn(request.getIsbn());
        book.setAvailableCopies(request.getAvailableCopies());
        return book;
    }

    public static Book toEntity(BookUpdateRequest request, Long id) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(request.getTitle());
        book.setAuthorId(request.getAuthorId());
        book.setGenre(request.getGenre());
        book.setPublishedYear(request.getPublishedYear());
        book.setIsbn(request.getIsbn());
        book.setAvailableCopies(request.getAvailableCopies());
        return book;
    }

    public static BookResponse toResponse(Book book) {
        BookResponse response = new BookResponse();
        response.setId(book.getId());
        response.setTitle(book.getTitle());
        response.setAuthorId(book.getAuthorId());
        response.setGenre(book.getGenre());
        response.setPublishedYear(book.getPublishedYear());
        response.setIsbn(book.getIsbn());
        response.setAvailableCopies(book.getAvailableCopies());
        return response;
    }
}
