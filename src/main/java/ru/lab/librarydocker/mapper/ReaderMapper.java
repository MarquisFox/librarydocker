package ru.lab.librarydocker.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.lab.librarydocker.entity.Reader;
import ru.lab.librarydocker.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ReaderMapper implements RowMapper<Reader> {

    @Override
    public Reader mapRow(ResultSet rs, int rowNum) throws SQLException {
        Reader reader = new Reader();
        reader.setId(SqlUtils.getLong(rs, "id"));
        reader.setFirstName(rs.getString("first_name"));
        reader.setLastName(rs.getString("last_name"));
        reader.setEmail(rs.getString("email"));
        reader.setPhone(rs.getString("phone"));
        reader.setRegistrationDate(SqlUtils.getLocalDate(rs, "registration_date"));
        return reader;
    }
}
