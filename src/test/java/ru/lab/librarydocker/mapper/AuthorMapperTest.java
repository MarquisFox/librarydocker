package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.entity.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorMapperTest {

    @Mock
    private ResultSet rs;

    private final AuthorMapper mapper = new AuthorMapper();

    @Test
    void mapRow_shouldMapAllColumns() throws SQLException {
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.wasNull()).thenReturn(false);
        when(rs.getString("name")).thenReturn("Leo Tolstoy");
        when(rs.getDate("birth_date")).thenReturn(java.sql.Date.valueOf("1828-09-09"));
        when(rs.getString("biography")).thenReturn("Russian writer");

        Author author = mapper.mapRow(rs, 1);

        assertThat(author.getId()).isEqualTo(1L);
        assertThat(author.getName()).isEqualTo("Leo Tolstoy");
        assertThat(author.getBirthDate()).isEqualTo(LocalDate.of(1828, 9, 9));
        assertThat(author.getBiography()).isEqualTo("Russian writer");
    }

    @Test
    void mapRow_shouldHandleNullValues() throws SQLException {
        when(rs.getLong("id")).thenReturn(0L);
        when(rs.wasNull()).thenReturn(true);
        when(rs.getString("name")).thenReturn(null);
        when(rs.getDate("birth_date")).thenReturn(null);
        when(rs.getString("biography")).thenReturn(null);

        Author author = mapper.mapRow(rs, 1);

        assertThat(author.getId()).isNull();
        assertThat(author.getName()).isNull();
        assertThat(author.getBirthDate()).isNull();
        assertThat(author.getBiography()).isNull();
    }
}