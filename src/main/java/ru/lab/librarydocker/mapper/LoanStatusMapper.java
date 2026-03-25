package ru.lab.librarydocker.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.lab.librarydocker.entity.LoanStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LoanStatusMapper implements RowMapper<LoanStatus> {

    @Override
    public LoanStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoanStatus status = new LoanStatus();
        status.setId(rs.getLong("id"));
        status.setName(rs.getString("name"));
        return status;
    }
}
