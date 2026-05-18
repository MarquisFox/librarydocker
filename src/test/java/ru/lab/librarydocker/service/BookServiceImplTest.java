package ru.lab.librarydocker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.dto.request.BookCreateRequest;
import ru.lab.librarydocker.dto.request.BookUpdateRequest;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.entity.Book;
import ru.lab.librarydocker.exception.ResourceNotFoundException;
import ru.lab.librarydocker.repository.AuthorRepository;
import ru.lab.librarydocker.repository.BookRepository;
import ru.lab.librarydocker.service.impl.BookServiceImpl;
import ru.lab.librarydocker.utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_shouldReturnList() {
        BookResponse response1 = new BookResponse();
        response1.setId(1L);
        BookResponse response2 = new BookResponse();
        response2.setId(2L);
        when(bookRepository.findAllWithAuthorNames()).thenReturn(List.of(response1, response2));

        List<BookResponse> result = bookService.getAllBooks();
        assertThat(result).hasSize(2);
        verify(bookRepository).findAllWithAuthorNames();
    }

    @Test
    void getBookById_whenExists_shouldReturn() {
        BookResponse response = new BookResponse();
        response.setId(10L);
        when(bookRepository.findByIdWithAuthorName(10L)).thenReturn(Optional.of(response));

        BookResponse result = bookService.getBookById(10L);
        assertThat(result.getId()).isEqualTo(10L);
    }

    @Test
    void getBookById_whenNotFound_shouldThrow() {
        when(bookRepository.findByIdWithAuthorName(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createBook_shouldValidateIsbnAndAuthorExistence() {
        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("Test Book");
        request.setAuthorId(5L);
        request.setIsbn("9781234567890");

        when(authorRepository.existsById(5L)).thenReturn(true);
        Book savedBook = new Book();
        savedBook.setId(100L);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);
        BookResponse expectedResponse = new BookResponse();
        expectedResponse.setId(100L);
        when(bookRepository.findByIdWithAuthorName(100L)).thenReturn(Optional.of(expectedResponse));

        BookResponse result = bookService.createBook(request);

        assertThat(result.getId()).isEqualTo(100L);
        verify(authorRepository).existsById(5L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_whenAuthorNotFound_shouldThrow() {
        BookCreateRequest request = new BookCreateRequest();
        request.setAuthorId(99L);
        when(authorRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.createBook(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Author not found with id: 99");
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_shouldCheckExistence() {
        Long id = 1L;
        BookUpdateRequest request = new BookUpdateRequest();
        request.setAuthorId(2L);
        when(bookRepository.existsById(id)).thenReturn(true);
        when(authorRepository.existsById(2L)).thenReturn(true);
        Book updatedBook = new Book();
        updatedBook.setId(id);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        BookResponse expectedResponse = new BookResponse();
        expectedResponse.setId(id);
        when(bookRepository.findByIdWithAuthorName(id)).thenReturn(Optional.of(expectedResponse));

        BookResponse result = bookService.updateBook(id, request);
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void updateBook_whenBookNotFound_shouldThrow() {
        when(bookRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> bookService.updateBook(99L, new BookUpdateRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteBook_whenExists_shouldDelete() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        bookService.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_whenNotFound_shouldThrow() {
        when(bookRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> bookService.deleteBook(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void findBooksByAuthor_shouldCallRepository() {
        bookService.findBooksByAuthor(1L);
        verify(bookRepository).findByAuthorIdWithAuthorName(1L);
    }

    @Test
    void findBooksByGenre_shouldCallRepository() {
        bookService.findBooksByGenre("Fiction");
        verify(bookRepository).findByGenreWithAuthorName("Fiction");
    }

    @Test
    void findBooksByAuthorAndGenre_shouldCallRepository() {
        bookService.findBooksByAuthorAndGenre(1L, "Novel");
        verify(bookRepository).findByAuthorIdAndGenreWithAuthorName(1L, "Novel");
    }
}