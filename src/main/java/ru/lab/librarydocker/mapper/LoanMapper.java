package ru.lab.librarydocker.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.lab.librarydocker.entity.Loan;
import ru.lab.librarydocker.utils.SqlUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LoanMapper implements RowMapper<Loan> {

    @Override
    public Loan mapRow(ResultSet rs, int rowNum) throws SQLException {
        Loan loan = new Loan();
        loan.setId(SqlUtils.getLong(rs, "id"));
        loan.setBookId(SqlUtils.getLong(rs, "book_id"));
        loan.setReaderId(SqlUtils.getLong(rs, "reader_id"));
        loan.setLoanDate(SqlUtils.getLocalDate(rs, "loan_date"));
        loan.setDueDate(SqlUtils.getLocalDate(rs, "due_date"));
        loan.setReturnDate(SqlUtils.getLocalDate(rs, "return_date"));
        loan.setStatusId(SqlUtils.getLong(rs, "status_id"));
        return loan;
    }
}
