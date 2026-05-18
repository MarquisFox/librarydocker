package ru.lab.librarydocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.lab.librarydocker.dto.request.AuthorCreateRequest;
import ru.lab.librarydocker.dto.request.AuthorUpdateRequest;
import ru.lab.librarydocker.dto.response.AuthorResponse;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.service.AuthorService;
import ru.lab.librarydocker.service.BookService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void getAllAuthors_shouldReturnList() throws Exception {
        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        response.setName("Test");
        when(authorService.getAllAuthors()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getAuthorById_shouldReturnAuthor() throws Exception {
        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        when(authorService.getAuthorById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createAuthor_shouldReturnCreated() throws Exception {
        AuthorCreateRequest request = new AuthorCreateRequest();
        request.setName("New Author");
        request.setBirthDate(LocalDate.of(2000, 1, 1));

        AuthorResponse response = new AuthorResponse();
        response.setId(10L);
        when(authorService.createAuthor(any(AuthorCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }


    @Test
    void updateAuthor_shouldReturnOk() throws Exception {
        AuthorUpdateRequest request = new AuthorUpdateRequest();
        request.setName("Updated");

        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        when(authorService.updateAuthor(eq(1L), any(AuthorUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteAuthor_shouldReturnNoContent() throws Exception {
        doNothing().when(authorService).deleteAuthor(1L);
        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBooksByAuthor_shouldReturnList() throws Exception {
        BookResponse book = new BookResponse();
        book.setId(100L);
        when(bookService.findBooksByAuthor(1L)).thenReturn(List.of(book));

        mockMvc.perform(get("/api/authors/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100));
    }
}
