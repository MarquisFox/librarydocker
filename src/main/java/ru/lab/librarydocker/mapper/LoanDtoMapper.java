package ru.lab.librarydocker.mapper;

import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.entity.Loan;

public final class LoanDtoMapper {

    private LoanDtoMapper() {}

    public static LoanResponse toResponse(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setBookId(loan.getBookId());
        response.setReaderId(loan.getReaderId());
        response.setLoanDate(loan.getLoanDate());
        response.setDueDate(loan.getDueDate());
        response.setReturnDate(loan.getReturnDate());
        response.setStatusId(loan.getStatusId());
        return response;
    }
}
