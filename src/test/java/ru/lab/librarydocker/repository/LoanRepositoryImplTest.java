package ru.lab.librarydocker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.entity.Loan;
import ru.lab.librarydocker.mapper.LoanMapper;
import ru.lab.librarydocker.mapper.LoanWithDetailsMapper;
import ru.lab.librarydocker.repository.impl.LoanRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({LoanRepositoryImpl.class, LoanMapper.class, LoanWithDetailsMapper.class})
class LoanRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private LoanRepositoryImpl loanRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM loans");
        jdbcTemplate.execute("DELETE FROM books");
        jdbcTemplate.execute("DELETE FROM readers");
        jdbcTemplate.execute("DELETE FROM loan_statuses");
        jdbcTemplate.execute("DELETE FROM authors");

        jdbcTemplate.update("INSERT INTO authors (id, name) VALUES (1, 'Author1')");
        jdbcTemplate.update("INSERT INTO loan_statuses (id, name) VALUES (1, 'ACTIVE'), (2, 'RETURNED')");
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (10, 'Book1', 1)");
        jdbcTemplate.update("INSERT INTO readers (id, first_name, last_name) VALUES (20, 'Reader', 'One')");
    }

    @Test
    void save_shouldInsertAndGenerateId() {
        Loan loan = new Loan();
        loan.setBookId(10L);
        loan.setReaderId(20L);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatusId(1L);

        Loan saved = loanRepository.save(loan);

        assertThat(saved.getId()).isNotNull();
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM loans", Integer.class)).isEqualTo(1);
    }

    @Test
    void save_shouldUpdateExisting() {
        Loan loan = new Loan();
        loan.setBookId(10L);
        loan.setReaderId(20L);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatusId(1L);
        Loan inserted = loanRepository.save(loan);
        Long id = inserted.getId();

        inserted.setDueDate(LocalDate.now().plusDays(7));
        inserted.setStatusId(2L);
        Loan updated = loanRepository.save(inserted);

        assertThat(updated.getDueDate()).isEqualTo(LocalDate.now().plusDays(7));
        assertThat(updated.getStatusId()).isEqualTo(2L);
        assertThat(jdbcTemplate.queryForObject("SELECT status_id FROM loans WHERE id = ?", Integer.class, id)).isEqualTo(2);
    }

    @Test
    void findById_shouldReturnLoanWhenExists() {
        Loan loan = new Loan();
        loan.setBookId(10L);
        loan.setReaderId(20L);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatusId(1L);
        Loan saved = loanRepository.save(loan);

        Optional<Loan> found = loanRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getBookId()).isEqualTo(10L);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<Loan> found = loanRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllLoans() {
        Loan loan1 = new Loan();
        loan1.setBookId(10L);
        loan1.setReaderId(20L);
        loan1.setLoanDate(LocalDate.now());
        loan1.setDueDate(LocalDate.now().plusDays(14));
        loan1.setStatusId(1L);
        loanRepository.save(loan1);

        Loan loan2 = new Loan();
        loan2.setBookId(10L);
        loan2.setReaderId(20L);
        loan2.setLoanDate(LocalDate.now());
        loan2.setDueDate(LocalDate.now().plusDays(14));
        loan2.setStatusId(2L);
        loanRepository.save(loan2);

        List<Loan> loans = loanRepository.findAll();
        assertThat(loans).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveLoan() {
        Loan loan = new Loan();
        loan.setBookId(10L);
        loan.setReaderId(20L);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatusId(1L);
        Loan saved = loanRepository.save(loan);

        loanRepository.deleteById(saved.getId());

        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM loans", Integer.class)).isZero();
    }

    @Test
    void findByReaderId_shouldReturnLoansForReader() {
        Loan loan1 = new Loan();
        loan1.setBookId(10L);
        loan1.setReaderId(20L);
        loan1.setLoanDate(LocalDate.now());
        loan1.setDueDate(LocalDate.now().plusDays(14));
        loan1.setStatusId(1L);
        loanRepository.save(loan1);

        Loan loan2 = new Loan();
        loan2.setBookId(10L);
        loan2.setReaderId(20L);
        loan2.setLoanDate(LocalDate.now());
        loan2.setDueDate(LocalDate.now().plusDays(14));
        loan2.setStatusId(2L);
        loanRepository.save(loan2);

        List<Loan> loans = loanRepository.findByReaderId(20L);
        assertThat(loans).hasSize(2);
    }

    @Test
    void findByBookId_shouldReturnLoansForBook() {
        Loan loan = new Loan();
        loan.setBookId(10L);
        loan.setReaderId(20L);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatusId(1L);
        loanRepository.save(loan);

        List<Loan> loans = loanRepository.findByBookId(10L);
        assertThat(loans).hasSize(1);
    }

    @Test
    void findAllWithDetails_shouldReturnPopulatedResponses() {
        Loan loan = new Loan();
        loan.setBookId(10L);
        loan.setReaderId(20L);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatusId(1L);
        loanRepository.save(loan);

        List<LoanResponse> responses = loanRepository.findAllWithDetails();
        assertThat(responses).hasSize(1);
        LoanResponse resp = responses.get(0);
        assertThat(resp.getBookTitle()).isEqualTo("Book1");
        assertThat(resp.getReaderFullName()).isEqualTo("Reader One");
        assertThat(resp.getStatusName()).isEqualTo("ACTIVE");
    }

    @Test
    void findByIdWithDetails_shouldReturnPopulatedResponse() {
        Loan loan = new Loan();
        loan.setBookId(10L);
        loan.setReaderId(20L);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatusId(1L);
        Loan saved = loanRepository.save(loan);

        Optional<LoanResponse> resp = loanRepository.findByIdWithDetails(saved.getId());
        assertThat(resp).isPresent();
        assertThat(resp.get().getBookTitle()).isEqualTo("Book1");
    }
}