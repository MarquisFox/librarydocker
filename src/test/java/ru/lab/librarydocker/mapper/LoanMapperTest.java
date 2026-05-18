package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.entity.Loan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanMapperTest {

    @Mock
    private ResultSet rs;

    private final LoanMapper mapper = new LoanMapper();

    @Test
    void mapRow_shouldMapAllColumns() throws SQLException {
        when(rs.getLong("id")).thenReturn(5L);
        when(rs.wasNull()).thenReturn(false);
        when(rs.getLong("book_id")).thenReturn(3L);
        when(rs.getLong("reader_id")).thenReturn(8L);
        when(rs.getDate("loan_date")).thenReturn(java.sql.Date.valueOf("2025-03-01"));
        when(rs.getDate("due_date")).thenReturn(java.sql.Date.valueOf("2025-03-15"));
        when(rs.getDate("return_date")).thenReturn(null);
        when(rs.getLong("status_id")).thenReturn(1L);

        Loan loan = mapper.mapRow(rs, 1);

        assertThat(loan.getId()).isEqualTo(5L);
        assertThat(loan.getBookId()).isEqualTo(3L);
        assertThat(loan.getReaderId()).isEqualTo(8L);
        assertThat(loan.getLoanDate()).isEqualTo(LocalDate.of(2025, 3, 1));
        assertThat(loan.getDueDate()).isEqualTo(LocalDate.of(2025, 3, 15));
        assertThat(loan.getReturnDate()).isNull();
        assertThat(loan.getStatusId()).isEqualTo(1L);
    }
}