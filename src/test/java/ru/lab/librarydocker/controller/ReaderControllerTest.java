package ru.lab.librarydocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.lab.librarydocker.dto.request.ReaderCreateRequest;
import ru.lab.librarydocker.dto.request.ReaderUpdateRequest;
import ru.lab.librarydocker.dto.response.ReaderResponse;
import ru.lab.librarydocker.service.ReaderService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReaderController.class)
class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReaderService readerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllReaders_shouldReturnList() throws Exception {
        when(readerService.getAllReaders()).thenReturn(List.of(new ReaderResponse()));
        mockMvc.perform(get("/api/readers"))
                .andExpect(status().isOk());
    }

    @Test
    void getReaderById_shouldReturnReader() throws Exception {
        ReaderResponse response = new ReaderResponse();
        response.setId(1L);
        when(readerService.getReaderById(1L)).thenReturn(response);
        mockMvc.perform(get("/api/readers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createReader_shouldReturnCreated() throws Exception {
        ReaderCreateRequest request = new ReaderCreateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");

        ReaderResponse response = new ReaderResponse();
        response.setId(10L);
        when(readerService.createReader(any(ReaderCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/readers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void updateReader_shouldReturnOk() throws Exception {
        ReaderUpdateRequest request = new ReaderUpdateRequest();
        request.setFirstName("Updated");
        request.setLastName("User");           // обязательно, если есть @NotBlank
        request.setEmail("updated@example.com");
        request.setPhone("+79111234567");

        ReaderResponse response = new ReaderResponse();
        response.setId(1L);
        when(readerService.updateReader(eq(1L), any(ReaderUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/readers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteReader_shouldReturnNoContent() throws Exception {
        doNothing().when(readerService).deleteReader(1L);
        mockMvc.perform(delete("/api/readers/1"))
                .andExpect(status().isNoContent());
    }
}
