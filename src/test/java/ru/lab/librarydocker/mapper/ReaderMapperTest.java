package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.entity.Reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReaderMapperTest {

    @Mock
    private ResultSet rs;

    private final ReaderMapper mapper = new ReaderMapper();

    @Test
    void mapRow_shouldMapAllColumns() throws SQLException {
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.wasNull()).thenReturn(false);
        when(rs.getString("first_name")).thenReturn("Anna");
        when(rs.getString("last_name")).thenReturn("Karenina");
        when(rs.getString("email")).thenReturn("anna@example.com");
        when(rs.getString("phone")).thenReturn("+71234567890");
        when(rs.getDate("registration_date")).thenReturn(java.sql.Date.valueOf("2025-01-15"));

        Reader reader = mapper.mapRow(rs, 1);

        assertThat(reader.getId()).isEqualTo(1L);
        assertThat(reader.getFirstName()).isEqualTo("Anna");
        assertThat(reader.getLastName()).isEqualTo("Karenina");
        assertThat(reader.getEmail()).isEqualTo("anna@example.com");
        assertThat(reader.getPhone()).isEqualTo("+71234567890");
        assertThat(reader.getRegistrationDate()).isEqualTo(LocalDate.of(2025, 1, 15));
    }
}