package ru.lab.librarydocker.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SqlUtilsTest {

    @Mock
    private ResultSet rs;

    @Test
    void getLong_shouldReturnLongValue_whenColumnExistsAndNotNull() throws SQLException {
        when(rs.getLong("col")).thenReturn(123L);
        when(rs.wasNull()).thenReturn(false);

        Long result = SqlUtils.getLong(rs, "col");

        assertThat(result).isEqualTo(123L);
    }

    @Test
    void getLong_shouldReturnNull_whenColumnIsNull() throws SQLException {
        when(rs.getLong("col")).thenReturn(0L);
        when(rs.wasNull()).thenReturn(true);

        Long result = SqlUtils.getLong(rs, "col");

        assertThat(result).isNull();
    }

    @Test
    void getInt_shouldReturnIntValue_whenColumnExistsAndNotNull() throws SQLException {
        when(rs.getInt("col")).thenReturn(42);
        when(rs.wasNull()).thenReturn(false);

        Integer result = SqlUtils.getInt(rs, "col");

        assertThat(result).isEqualTo(42);
    }

    @Test
    void getInt_shouldReturnNull_whenColumnIsNull() throws SQLException {
        when(rs.getInt("col")).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);

        Integer result = SqlUtils.getInt(rs, "col");

        assertThat(result).isNull();
    }

    @Test
    void getLocalDate_shouldReturnLocalDate_whenColumnExistsAndNotNull() throws SQLException {
        java.sql.Date sqlDate = java.sql.Date.valueOf("2025-03-25");
        when(rs.getDate("col")).thenReturn(sqlDate);

        LocalDate result = SqlUtils.getLocalDate(rs, "col");

        assertThat(result).isEqualTo(LocalDate.of(2025, 3, 25));
    }

    @Test
    void getLocalDate_shouldReturnNull_whenColumnIsNull() throws SQLException {
        when(rs.getDate("col")).thenReturn(null);

        LocalDate result = SqlUtils.getLocalDate(rs, "col");

        assertThat(result).isNull();
    }
}