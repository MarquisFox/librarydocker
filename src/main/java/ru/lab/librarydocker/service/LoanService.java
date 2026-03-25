package ru.lab.librarydocker.service;

import ru.lab.librarydocker.dto.request.LoanCreateRequest;
import ru.lab.librarydocker.dto.response.LoanResponse;

import java.util.List;

public interface LoanService {
    List<LoanResponse> getAllLoans();
    LoanResponse getLoanById(Long id);
    LoanResponse issueBook(LoanCreateRequest request);
    LoanResponse returnBook(Long loanId);
    List<LoanResponse> getLoansByReader(Long readerId);
    List<LoanResponse> getLoansByBook(Long bookId);
}
