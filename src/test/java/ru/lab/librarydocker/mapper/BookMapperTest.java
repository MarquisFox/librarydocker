package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.entity.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookMapperTest {

    @Mock
    private ResultSet rs;

    private final BookMapper mapper = new BookMapper();

    @Test
    void mapRow_shouldMapAllColumns() throws SQLException {
        when(rs.getLong("id")).thenReturn(10L);
        when(rs.wasNull()).thenReturn(false);
        when(rs.getString("title")).thenReturn("Crime and Punishment");
        when(rs.getLong("author_id")).thenReturn(3L);
        when(rs.getString("genre")).thenReturn("Psychological fiction");
        when(rs.getInt("published_year")).thenReturn(1866);
        when(rs.getString("isbn")).thenReturn("9780140449136");
        when(rs.getInt("available_copies")).thenReturn(2);

        Book book = mapper.mapRow(rs, 1);

        assertThat(book.getId()).isEqualTo(10L);
        assertThat(book.getTitle()).isEqualTo("Crime and Punishment");
        assertThat(book.getAuthorId()).isEqualTo(3L);
        assertThat(book.getGenre()).isEqualTo("Psychological fiction");
        assertThat(book.getPublishedYear()).isEqualTo(1866);
        assertThat(book.getIsbn()).isEqualTo("9780140449136");
        assertThat(book.getAvailableCopies()).isEqualTo(2);
    }

    @Test
    void mapRow_shouldHandleNulls() throws SQLException {
        when(rs.getLong("id")).thenReturn(0L);
        when(rs.wasNull()).thenReturn(true);
        when(rs.getString("title")).thenReturn(null);
        when(rs.getLong("author_id")).thenReturn(0L);
        when(rs.wasNull()).thenReturn(true);
        when(rs.getString("genre")).thenReturn(null);
        when(rs.getInt("published_year")).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);
        when(rs.getString("isbn")).thenReturn(null);
        when(rs.getInt("available_copies")).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);

        Book book = mapper.mapRow(rs, 1);

        assertThat(book.getId()).isNull();
        assertThat(book.getTitle()).isNull();
        assertThat(book.getAuthorId()).isNull();
        assertThat(book.getGenre()).isNull();
        assertThat(book.getPublishedYear()).isNull();
        assertThat(book.getIsbn()).isNull();
        assertThat(book.getAvailableCopies()).isNull();
    }
}