package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.dto.response.LoanResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanWithDetailsMapperTest {

    @Mock
    private ResultSet rs;

    private final LoanWithDetailsMapper mapper = new LoanWithDetailsMapper();

    @Test
    void mapRow_shouldMapAllColumnsWithJoins() throws SQLException {
        when(rs.getLong("loan_id")).thenReturn(1L);
        when(rs.wasNull()).thenReturn(false);
        when(rs.getLong("book_id")).thenReturn(10L);
        when(rs.getString("book_title")).thenReturn("War and Peace");
        when(rs.getLong("reader_id")).thenReturn(20L);
        when(rs.getString("reader_full_name")).thenReturn("Ivan Petrov");
        when(rs.getDate("loan_date")).thenReturn(java.sql.Date.valueOf("2025-03-10"));
        when(rs.getDate("due_date")).thenReturn(java.sql.Date.valueOf("2025-03-24"));
        when(rs.getDate("return_date")).thenReturn(null);
        when(rs.getLong("status_id")).thenReturn(1L);
        when(rs.getString("status_name")).thenReturn("ACTIVE");

        LoanResponse response = mapper.mapRow(rs, 1);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBookId()).isEqualTo(10L);
        assertThat(response.getBookTitle()).isEqualTo("War and Peace");
        assertThat(response.getReaderId()).isEqualTo(20L);
        assertThat(response.getReaderFullName()).isEqualTo("Ivan Petrov");
        assertThat(response.getLoanDate()).isEqualTo(LocalDate.of(2025, 3, 10));
        assertThat(response.getDueDate()).isEqualTo(LocalDate.of(2025, 3, 24));
        assertThat(response.getReturnDate()).isNull();
        assertThat(response.getStatusId()).isEqualTo(1L);
        assertThat(response.getStatusName()).isEqualTo("ACTIVE");
    }
}