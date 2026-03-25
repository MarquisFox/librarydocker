package ru.lab.librarydocker.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.lab.librarydocker.dto.response.LoanResponse;
import ru.lab.librarydocker.entity.Loan;
import ru.lab.librarydocker.exception.BusinessException;
import ru.lab.librarydocker.mapper.LoanMapper;
import ru.lab.librarydocker.mapper.LoanWithDetailsMapper;
import ru.lab.librarydocker.repository.LoanRepository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class LoanRepositoryImpl implements LoanRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LoanMapper loanMapper;
    private final LoanWithDetailsMapper loanWithDetailsMapper;

    public LoanRepositoryImpl(JdbcTemplate jdbcTemplate, LoanMapper loanMapper, LoanWithDetailsMapper loanWithDetailsMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.loanMapper = loanMapper;
        this.loanWithDetailsMapper = loanWithDetailsMapper;
    }

    @Override
    public List<Loan> findAll() {
        String sql = "SELECT * FROM loans";
        return jdbcTemplate.query(sql, loanMapper);
    }

    @Override
    public Optional<Loan> findById(Long id) {
        String sql = "SELECT * FROM loans WHERE id = ?";
        List<Loan> loans = jdbcTemplate.query(sql, loanMapper, id);
        return loans.stream().findFirst();
    }

    @Override
    public Loan save(Loan loan) {
        if (loan.getId() == null) {
            String sql = "INSERT INTO loans (book_id, reader_id, loan_date, due_date, return_date, status_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // ← добавить флаг
                ps.setLong(1, loan.getBookId());
                ps.setLong(2, loan.getReaderId());
                ps.setObject(3, loan.getLoanDate());
                ps.setObject(4, loan.getDueDate());
                ps.setObject(5, loan.getReturnDate());
                ps.setLong(6, loan.getStatusId());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new BusinessException("Failed to generate loan ID");
            }
            loan.setId(key.longValue());
        }else {
            String sql = "UPDATE loans SET book_id = ?, reader_id = ?, loan_date = ?, due_date = ?, return_date = ?, status_id = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sql,
                    loan.getBookId(), loan.getReaderId(), loan.getLoanDate(),
                    loan.getDueDate(), loan.getReturnDate(), loan.getStatusId(),
                    loan.getId());
        }
        return loan;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM loans WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Loan> findByReaderId(Long readerId) {
        String sql = "SELECT * FROM loans WHERE reader_id = ?";
        return jdbcTemplate.query(sql, loanMapper, readerId);
    }

    @Override
    public List<Loan> findByBookId(Long bookId) {
        String sql = "SELECT * FROM loans WHERE book_id = ?";
        return jdbcTemplate.query(sql, loanMapper, bookId);
    }

    @Override
    public List<LoanResponse> findAllWithDetails() {
        String sql = """
            SELECT l.id AS loan_id,
                   l.book_id,
                   b.title AS book_title,
                   l.reader_id,
                   CONCAT(r.first_name, ' ', r.last_name) AS reader_full_name,
                   l.loan_date,
                   l.due_date,
                   l.return_date,
                   l.status_id,
                   s.name AS status_name
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN readers r ON l.reader_id = r.id
            JOIN loan_statuses s ON l.status_id = s.id
            """;
        return jdbcTemplate.query(sql, loanWithDetailsMapper);
    }

    @Override
    public Optional<LoanResponse> findByIdWithDetails(Long id) {
        String sql = """
            SELECT l.id AS loan_id,
                   l.book_id,
                   b.title AS book_title,
                   l.reader_id,
                   CONCAT(r.first_name, ' ', r.last_name) AS reader_full_name,
                   l.loan_date,
                   l.due_date,
                   l.return_date,
                   l.status_id,
                   s.name AS status_name
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN readers r ON l.reader_id = r.id
            JOIN loan_statuses s ON l.status_id = s.id
            WHERE l.id = ?
            """;
        List<LoanResponse> results = jdbcTemplate.query(sql, loanWithDetailsMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<LoanResponse> findByReaderIdWithDetails(Long readerId) {
        String sql = """
            SELECT l.id AS loan_id,
                   l.book_id,
                   b.title AS book_title,
                   l.reader_id,
                   CONCAT(r.first_name, ' ', r.last_name) AS reader_full_name,
                   l.loan_date,
                   l.due_date,
                   l.return_date,
                   l.status_id,
                   s.name AS status_name
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN readers r ON l.reader_id = r.id
            JOIN loan_statuses s ON l.status_id = s.id
            WHERE l.reader_id = ?
            """;
        return jdbcTemplate.query(sql, loanWithDetailsMapper, readerId);
    }

    @Override
    public List<LoanResponse> findByBookIdWithDetails(Long bookId) {
        String sql = """
            SELECT l.id AS loan_id,
                   l.book_id,
                   b.title AS book_title,
                   l.reader_id,
                   CONCAT(r.first_name, ' ', r.last_name) AS reader_full_name,
                   l.loan_date,
                   l.due_date,
                   l.return_date,
                   l.status_id,
                   s.name AS status_name
            FROM loans l
            JOIN books b ON l.book_id = b.id
            JOIN readers r ON l.reader_id = r.id
            JOIN loan_statuses s ON l.status_id = s.id
            WHERE l.book_id = ?
            """;
        return jdbcTemplate.query(sql, loanWithDetailsMapper, bookId);
    }
}
