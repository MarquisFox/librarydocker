package ru.lab.librarydocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.lab.librarydocker.dto.request.BookCreateRequest;
import ru.lab.librarydocker.dto.request.BookUpdateRequest;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.service.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllBooks_shouldReturnList() throws Exception {
        BookResponse book = new BookResponse();
        book.setId(1L);
        when(bookService.getAllBooks()).thenReturn(List.of(book));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getBookById_shouldReturnBook() throws Exception {
        BookResponse book = new BookResponse();
        book.setId(2L);
        when(bookService.getBookById(2L)).thenReturn(book);

        mockMvc.perform(get("/api/books/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void createBook_shouldReturnCreated() throws Exception {
        BookCreateRequest request = new BookCreateRequest();
        request.setTitle("New Book");
        request.setAuthorId(1L);

        BookResponse response = new BookResponse();
        response.setId(10L);
        when(bookService.createBook(any(BookCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void updateBook_shouldReturnOk() throws Exception {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setTitle("Updated");
        request.setAuthorId(1L);          // обязательно, если в DTO есть @NotNull
        request.setGenre("Fiction");
        request.setPublishedYear(2000);
        request.setIsbn("9781234567890");
        request.setAvailableCopies(5);

        BookResponse response = new BookResponse();
        response.setId(1L);
        when(bookService.updateBook(eq(1L), any(BookUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    void deleteBook_shouldReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBook(1L);
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchBooks_withBothParams_shouldCallFindByAuthorAndGenre() throws Exception {
        when(bookService.findBooksByAuthorAndGenre(1L, "Fiction")).thenReturn(List.of(new BookResponse()));
        mockMvc.perform(get("/api/books/search?authorId=1&genre=Fiction"))
                .andExpect(status().isOk());
    }

    @Test
    void searchBooks_withOnlyAuthorId_shouldCallFindByAuthor() throws Exception {
        when(bookService.findBooksByAuthor(1L)).thenReturn(List.of(new BookResponse()));
        mockMvc.perform(get("/api/books/search?authorId=1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchBooks_withOnlyGenre_shouldCallFindByGenre() throws Exception {
        when(bookService.findBooksByGenre("Fiction")).thenReturn(List.of(new BookResponse()));
        mockMvc.perform(get("/api/books/search?genre=Fiction"))
                .andExpect(status().isOk());
    }

    @Test
    void searchBooks_withoutParams_shouldCallGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(new BookResponse()));
        mockMvc.perform(get("/api/books/search"))
                .andExpect(status().isOk());
    }
}