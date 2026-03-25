package ru.lab.librarydocker.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookWithAuthorMapper implements RowMapper<BookResponse> {

    @Override
    public BookResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        BookResponse response = new BookResponse();
        response.setId(SqlUtils.getLong(rs, "id"));
        response.setTitle(rs.getString("title"));
        response.setAuthorId(SqlUtils.getLong(rs, "author_id"));
        response.setAuthorName(rs.getString("author_name"));
        response.setGenre(rs.getString("genre"));
        response.setPublishedYear(SqlUtils.getInt(rs, "published_year"));
        response.setIsbn(rs.getString("isbn"));
        response.setAvailableCopies(SqlUtils.getInt(rs, "available_copies"));
        return response;
    }
}
