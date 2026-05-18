package ru.lab.librarydocker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import ru.lab.librarydocker.service.impl.LoanServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReaderRepository readerRepository;
    @Mock
    private LoanStatusRepository loanStatusRepository;
    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    void getAllLoans_shouldReturnList() {
        List<LoanResponse> mockList = List.of(new LoanResponse(), new LoanResponse());
        when(loanRepository.findAllWithDetails()).thenReturn(mockList);
        assertThat(loanService.getAllLoans()).hasSize(2);
    }

    @Test
    void getLoanById_whenExists_shouldReturn() {
        LoanResponse response = new LoanResponse();
        response.setId(1L);
        when(loanRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(response));
        assertThat(loanService.getLoanById(1L).getId()).isEqualTo(1L);
    }

    @Test
    void getLoanById_whenNotFound_shouldThrow() {
        when(loanRepository.findByIdWithDetails(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> loanService.getLoanById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void issueBook_shouldSuccess_whenAllConditionsMet() {
        LoanCreateRequest request = new LoanCreateRequest();
        request.setBookId(1L);
        request.setReaderId(2L);
        request.setLoanDays(7);

        Book book = new Book();
        book.setId(1L);
        book.setAvailableCopies(3);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.existsById(2L)).thenReturn(true);

        LoanStatus overdue = new LoanStatus();
        overdue.setId(3L);
        when(loanStatusRepository.findByName("OVERDUE")).thenReturn(Optional.of(overdue));
        when(loanRepository.findByReaderId(2L)).thenReturn(List.of()); // no overdue

        LoanStatus active = new LoanStatus();
        active.setId(1L);
        when(loanStatusRepository.findByName("ACTIVE")).thenReturn(Optional.of(active));

        Loan savedLoan = new Loan();
        savedLoan.setId(10L);
        when(loanRepository.save(any(Loan.class))).thenReturn(savedLoan);

        LoanResponse expectedResponse = new LoanResponse();
        expectedResponse.setId(10L);
        when(loanRepository.findByIdWithDetails(10L)).thenReturn(Optional.of(expectedResponse));

        LoanResponse result = loanService.issueBook(request);

        assertThat(result.getId()).isEqualTo(10L);
        verify(bookRepository).save(book);
        assertThat(book.getAvailableCopies()).isEqualTo(2);
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void issueBook_shouldThrow_whenBookNotFound() {
        LoanCreateRequest request = new LoanCreateRequest();
        request.setBookId(99L);
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> loanService.issueBook(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void issueBook_shouldThrow_whenNoAvailableCopies() {
        Book book = new Book();
        book.setAvailableCopies(0);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        LoanCreateRequest request = new LoanCreateRequest();
        request.setBookId(1L);
        assertThatThrownBy(() -> loanService.issueBook(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("No available copies");
    }

    @Test
    void issueBook_shouldThrow_whenReaderNotFound() {
        Book book = new Book();
        book.setAvailableCopies(2);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        LoanCreateRequest request = new LoanCreateRequest();
        request.setBookId(1L);
        request.setReaderId(99L);
        when(readerRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> loanService.issueBook(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Reader not found");
    }

    @Test
    void issueBook_shouldThrow_whenReaderHasOverdueLoans() {
        Book book = new Book();
        book.setAvailableCopies(2);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(readerRepository.existsById(2L)).thenReturn(true);

        LoanStatus overdue = new LoanStatus();
        overdue.setId(3L);
        when(loanStatusRepository.findByName("OVERDUE")).thenReturn(Optional.of(overdue));

        Loan overdueLoan = new Loan();
        overdueLoan.setStatusId(3L);
        when(loanRepository.findByReaderId(2L)).thenReturn(List.of(overdueLoan));

        LoanCreateRequest request = new LoanCreateRequest();
        request.setBookId(1L);
        request.setReaderId(2L);
        assertThatThrownBy(() -> loanService.issueBook(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Reader has overdue books");
    }

    @Test
    void returnBook_shouldSuccess() {
        Loan loan = new Loan();
        loan.setId(10L);
        loan.setBookId(1L);
        loan.setReturnDate(null);
        loan.setStatusId(1L);

        LoanStatus active = new LoanStatus();
        active.setId(1L);
        when(loanStatusRepository.findByName("ACTIVE")).thenReturn(Optional.of(active));
        LoanStatus returned = new LoanStatus();
        returned.setId(2L);
        when(loanStatusRepository.findByName("RETURNED")).thenReturn(Optional.of(returned));

        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));
        Book book = new Book();
        book.setId(1L);
        book.setAvailableCopies(5);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        LoanResponse resp = new LoanResponse();
        resp.setId(10L);
        when(loanRepository.findByIdWithDetails(10L)).thenReturn(Optional.of(resp));

        LoanResponse result = loanService.returnBook(10L);
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(loan.getReturnDate()).isNotNull();
        assertThat(loan.getStatusId()).isEqualTo(2L);
        assertThat(book.getAvailableCopies()).isEqualTo(6);
        verify(bookRepository).save(book);
    }

    @Test
    void returnBook_shouldThrow_whenAlreadyReturned() {
        Loan loan = new Loan();
        loan.setReturnDate(LocalDate.now());
        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));
        assertThatThrownBy(() -> loanService.returnBook(10L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already returned");
    }

    @Test
    void returnBook_shouldThrow_whenLoanNotActive() {
        Loan loan = new Loan();
        loan.setReturnDate(null);
        loan.setStatusId(5L);
        when(loanRepository.findById(10L)).thenReturn(Optional.of(loan));
        LoanStatus active = new LoanStatus();
        active.setId(1L);
        when(loanStatusRepository.findByName("ACTIVE")).thenReturn(Optional.of(active));
        assertThatThrownBy(() -> loanService.returnBook(10L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Loan is not active");
    }

    @Test
    void getLoansByReader_shouldCallRepository() {
        when(readerRepository.existsById(1L)).thenReturn(true);
        loanService.getLoansByReader(1L);
        verify(loanRepository).findByReaderIdWithDetails(1L);
    }

    @Test
    void getLoansByBook_shouldCallRepository() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        loanService.getLoansByBook(1L);
        verify(loanRepository).findByBookIdWithDetails(1L);
    }
}
