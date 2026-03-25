package ru.lab.librarydocker.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.lab.librarydocker.entity.Book;
import ru.lab.librarydocker.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        Book book = new Book();
        book.setId(SqlUtils.getLong(rs, "id"));
        book.setTitle(rs.getString("title"));
        book.setAuthorId(SqlUtils.getLong(rs, "author_id"));
        book.setGenre(rs.getString("genre"));
        book.setPublishedYear(SqlUtils.getInt(rs, "published_year"));
        book.setIsbn(rs.getString("isbn"));
        book.setAvailableCopies(SqlUtils.getInt(rs, "available_copies"));
        return book;
    }
}

