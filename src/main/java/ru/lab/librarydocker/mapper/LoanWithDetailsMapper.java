package ru.lab.librarydocker.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LoanWithDetailsMapper implements RowMapper<LoanResponse> {

    @Override
    public LoanResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoanResponse response = new LoanResponse();
        response.setId(SqlUtils.getLong(rs, "loan_id"));
        response.setBookId(SqlUtils.getLong(rs, "book_id"));
        response.setBookTitle(rs.getString("book_title"));
        response.setReaderId(SqlUtils.getLong(rs, "reader_id"));
        response.setReaderFullName(rs.getString("reader_full_name"));
        response.setLoanDate(SqlUtils.getLocalDate(rs, "loan_date"));
        response.setDueDate(SqlUtils.getLocalDate(rs, "due_date"));
        response.setReturnDate(SqlUtils.getLocalDate(rs, "return_date"));
        response.setStatusId(SqlUtils.getLong(rs, "status_id"));
        response.setStatusName(rs.getString("status_name"));
        return response;
    }
}
