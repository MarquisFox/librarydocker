package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.entity.Loan;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class LoanDtoMapperTest {

    @Test
    void toResponse_shouldMapAllFields() {
        Loan loan = new Loan();
        loan.setId(10L);
        loan.setBookId(100L);
        loan.setReaderId(200L);
        loan.setLoanDate(LocalDate.of(2025, 1, 1));
        loan.setDueDate(LocalDate.of(2025, 1, 15));
        loan.setReturnDate(LocalDate.of(2025, 1, 10));
        loan.setStatusId(1L);

        LoanResponse response = LoanDtoMapper.toResponse(loan);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getBookId()).isEqualTo(100L);
        assertThat(response.getReaderId()).isEqualTo(200L);
        assertThat(response.getLoanDate()).isEqualTo("2025-01-01");
        assertThat(response.getDueDate()).isEqualTo("2025-01-15");
        assertThat(response.getReturnDate()).isEqualTo("2025-01-10");
        assertThat(response.getStatusId()).isEqualTo(1L);

        assertThat(response.getBookTitle()).isNull();
        assertThat(response.getReaderFullName()).isNull();
        assertThat(response.getStatusName()).isNull();
    }
}