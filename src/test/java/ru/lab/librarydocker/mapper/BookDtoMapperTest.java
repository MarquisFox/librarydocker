package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import ru.lab.librarydocker.dto.request.BookCreateRequest;
import ru.lab.librarydocker.dto.request.BookUpdateRequest;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.entity.Book;

import static org.assertj.core.api.Assertions.assertThat;

class BookDtoMapperTest {

    @Test
    void toEntity_fromCreateRequest_shouldMap() {
        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("War and Peace");
        request.setAuthorId(5L);
        request.setGenre("Novel");
        request.setPublishedYear(1869);
        request.setIsbn("9781234567897");
        request.setAvailableCopies(3);

        Book book = BookDtoMapper.toEntity(request);

        assertThat(book.getId()).isNull();
        assertThat(book.getTitle()).isEqualTo("War and Peace");
        assertThat(book.getAuthorId()).isEqualTo(5L);
        assertThat(book.getGenre()).isEqualTo("Novel");
        assertThat(book.getPublishedYear()).isEqualTo(1869);
        assertThat(book.getIsbn()).isEqualTo("9781234567897");
        assertThat(book.getAvailableCopies()).isEqualTo(3);
    }

    @Test
    void toEntity_fromUpdateRequest_shouldMapWithId() {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle("Updated Title");
        request.setAuthorId(10L);
        request.setGenre("Fiction");
        request.setPublishedYear(2000);
        request.setIsbn("1234567890");
        request.setAvailableCopies(1);

        Book book = BookDtoMapper.toEntity(request, 42L);

        assertThat(book.getId()).isEqualTo(42L);
        assertThat(book.getTitle()).isEqualTo("Updated Title");
        assertThat(book.getAuthorId()).isEqualTo(10L);
        assertThat(book.getGenre()).isEqualTo("Fiction");
        assertThat(book.getPublishedYear()).isEqualTo(2000);
        assertThat(book.getIsbn()).isEqualTo("1234567890");
        assertThat(book.getAvailableCopies()).isEqualTo(1);
    }

    @Test
    void toResponse_shouldMap() {
        Book book = new Book();
        book.setId(100L);
        book.setTitle("Anna Karenina");
        book.setAuthorId(2L);
        book.setGenre("Novel");
        book.setPublishedYear(1877);
        book.setIsbn("9780451524935");
        book.setAvailableCopies(5);

        BookResponse response = BookDtoMapper.toResponse(book);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getTitle()).isEqualTo("Anna Karenina");
        assertThat(response.getAuthorId()).isEqualTo(2L);
        assertThat(response.getGenre()).isEqualTo("Novel");
        assertThat(response.getPublishedYear()).isEqualTo(1877);
        assertThat(response.getIsbn()).isEqualTo("9780451524935");
        assertThat(response.getAvailableCopies()).isEqualTo(5);
        assertThat(response.getAuthorName()).isNull();
    }
}