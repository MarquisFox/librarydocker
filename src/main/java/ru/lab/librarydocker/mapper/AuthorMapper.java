package ru.lab.librarydocker.mapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.lab.librarydocker.entity.Author;
import ru.lab.librarydocker.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
        Author author = new Author();
        author.setId(SqlUtils.getLong(rs, "id"));
        author.setName(rs.getString("name"));
        author.setBirthDate(SqlUtils.getLocalDate(rs, "birth_date"));
        author.setBiography(rs.getString("biography"));
        return author;
    }
}
