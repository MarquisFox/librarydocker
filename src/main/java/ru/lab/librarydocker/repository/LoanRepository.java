package ru.lab.librarydocker.repository;

import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.entity.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {

    List<Loan> findAll();
    Optional<Loan> findById(Long id);
    Loan save(Loan loan);
    void deleteById(Long id);
    List<Loan> findByReaderId(Long readerId);
    List<Loan> findByBookId(Long bookId);


    List<LoanResponse> findAllWithDetails();
    Optional<LoanResponse> findByIdWithDetails(Long id);
    List<LoanResponse> findByReaderIdWithDetails(Long readerId);
    List<LoanResponse> findByBookIdWithDetails(Long bookId);
}
