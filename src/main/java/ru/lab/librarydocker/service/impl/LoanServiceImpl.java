package ru.lab.librarydocker.service.impl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lab.librarydocker.dto.request.LoanCreateRequest;
import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.entity.Book;
import ru.lab.librarydocker.entity.Loan;
import ru.lab.librarydocker.entity.LoanStatus;
import ru.lab.librarydocker.exception.BusinessException;
import ru.lab.librarydocker.exception.ResourceNotFoundException;
import ru.lab.librarydocker.repository.BookRepository;
import ru.lab.librarydocker.repository.LoanRepository;
import ru.lab.librarydocker.repository.LoanStatusRepository;
import ru.lab.librarydocker.repository.ReaderRepository;
import ru.lab.librarydocker.service.LoanService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final LoanStatusRepository loanStatusRepository;

    public LoanServiceImpl(LoanRepository loanRepository,
                           BookRepository bookRepository,
                           ReaderRepository readerRepository,
                           LoanStatusRepository loanStatusRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.loanStatusRepository = loanStatusRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponse> getAllLoans() {
        return loanRepository.findAllWithDetails();
    }

    @Override
    @Transactional(readOnly = true)
    public LoanResponse getLoanById(Long id) {
        return loanRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }

    @Override
    public LoanResponse issueBook(LoanCreateRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + request.getBookId()));
        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No available copies of this book");
        }

        if (!readerRepository.existsById(request.getReaderId())) {
            throw new ResourceNotFoundException("Reader not found: " + request.getReaderId());
        }

        LoanStatus overdueStatus = loanStatusRepository.findByName("OVERDUE")
                .orElseThrow(() -> new BusinessException("Loan status configuration missing"));
        List<Loan> activeOverdueLoans = loanRepository.findByReaderId(request.getReaderId()).stream()
                .filter(loan -> loan.getStatusId().equals(overdueStatus.getId()))
                .toList();
        if (!activeOverdueLoans.isEmpty()) {
            throw new BusinessException("Reader has overdue books, cannot issue new book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        LoanStatus activeStatus = loanStatusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new BusinessException("Loan status configuration missing"));
        int loanDays = request.getLoanDays() != null ? request.getLoanDays() : 14;

        Loan loan = new Loan();
        loan.setBookId(request.getBookId());
        loan.setReaderId(request.getReaderId());
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(loanDays));
        loan.setStatusId(activeStatus.getId());

        Loan saved = loanRepository.save(loan);

        return loanRepository.findByIdWithDetails(saved.getId())
                .orElseThrow(() -> new BusinessException("Loan not found after creation"));
    }

    @Override
    public LoanResponse returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + loanId));

        if (loan.getReturnDate() != null) {
            throw new BusinessException("Book already returned");
        }

        LoanStatus activeStatus = loanStatusRepository.findByName("ACTIVE")
                .orElseThrow(() -> new BusinessException("Loan status configuration missing"));
        if (!loan.getStatusId().equals(activeStatus.getId())) {
            throw new BusinessException("Loan is not active");
        }

        LoanStatus returnedStatus = loanStatusRepository.findByName("RETURNED")
                .orElseThrow(() -> new BusinessException("Loan status configuration missing"));
        loan.setStatusId(returnedStatus.getId());
        loan.setReturnDate(LocalDate.now());
        Loan updated = loanRepository.save(loan);

        Book book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + loan.getBookId()));
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.findByIdWithDetails(updated.getId())
                .orElseThrow(() -> new BusinessException("Loan not found after return"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponse> getLoansByReader(Long readerId) {
        if (!readerRepository.existsById(readerId)) {
            throw new ResourceNotFoundException("Reader not found: " + readerId);
        }
        return loanRepository.findByReaderIdWithDetails(readerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponse> getLoansByBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Book not found: " + bookId);
        }
        return loanRepository.findByBookIdWithDetails(bookId);
    }
}
