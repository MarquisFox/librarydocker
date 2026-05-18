package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.dto.response.BookResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookWithAuthorMapperTest {

    @Mock
    private ResultSet rs;

    private final BookWithAuthorMapper mapper = new BookWithAuthorMapper();

    @Test
    void mapRow_shouldMapAllColumnsIncludingAuthorName() throws SQLException {
        when(rs.getLong("id")).thenReturn(1L);
        when(rs.wasNull()).thenReturn(false);
        when(rs.getString("title")).thenReturn("The Idiot");
        when(rs.getLong("author_id")).thenReturn(2L);
        when(rs.getString("author_name")).thenReturn("Fyodor Dostoevsky");
        when(rs.getString("genre")).thenReturn("Novel");
        when(rs.getInt("published_year")).thenReturn(1869);
        when(rs.getString("isbn")).thenReturn("9780140447927");
        when(rs.getInt("available_copies")).thenReturn(5);

        BookResponse response = mapper.mapRow(rs, 1);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("The Idiot");
        assertThat(response.getAuthorId()).isEqualTo(2L);
        assertThat(response.getAuthorName()).isEqualTo("Fyodor Dostoevsky");
        assertThat(response.getGenre()).isEqualTo("Novel");
        assertThat(response.getPublishedYear()).isEqualTo(1869);
        assertThat(response.getIsbn()).isEqualTo("9780140447927");
        assertThat(response.getAvailableCopies()).isEqualTo(5);
    }
}