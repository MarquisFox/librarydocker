package ru.lab.librarydocker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.lab.librarydocker.dto.request.LoanCreateRequest;
import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.service.LoanService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanService loanService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllLoans_shouldReturnList() throws Exception {
        when(loanService.getAllLoans()).thenReturn(List.of(new LoanResponse()));
        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoanById_shouldReturnLoan() throws Exception {
        LoanResponse response = new LoanResponse();
        response.setId(1L);
        when(loanService.getLoanById(1L)).thenReturn(response);
        mockMvc.perform(get("/api/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void issueBook_shouldReturnCreated() throws Exception {
        LoanCreateRequest request = new LoanCreateRequest();
        request.setBookId(1L);
        request.setReaderId(2L);

        LoanResponse response = new LoanResponse();
        response.setId(10L);
        when(loanService.issueBook(any(LoanCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void returnBook_shouldReturnOk() throws Exception {
        LoanResponse response = new LoanResponse();
        response.setId(1L);
        when(loanService.returnBook(1L)).thenReturn(response);
        mockMvc.perform(put("/api/loans/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getLoansByReader_shouldReturnList() throws Exception {
        when(loanService.getLoansByReader(5L)).thenReturn(List.of(new LoanResponse()));
        mockMvc.perform(get("/api/loans/reader/5"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoansByBook_shouldReturnList() throws Exception {
        when(loanService.getLoansByBook(3L)).thenReturn(List.of(new LoanResponse()));
        mockMvc.perform(get("/api/loans/book/3"))
                .andExpect(status().isOk());
    }
}