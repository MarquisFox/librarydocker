package ru.lab.librarydocker.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.lab.librarydocker.entity.LoanStatus;
import ru.lab.librarydocker.mapper.LoanStatusMapper;
import ru.lab.librarydocker.repository.LoanStatusRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class LoanStatusRepositoryImpl implements LoanStatusRepository {

    private final JdbcTemplate jdbcTemplate;
    private final LoanStatusMapper loanStatusMapper;

    public LoanStatusRepositoryImpl(JdbcTemplate jdbcTemplate, LoanStatusMapper loanStatusMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.loanStatusMapper = loanStatusMapper;
    }

    @Override
    public List<LoanStatus> findAll() {
        String sql = "SELECT * FROM loan_statuses";
        return jdbcTemplate.query(sql, loanStatusMapper);
    }

    @Override
    public Optional<LoanStatus> findById(Long id) {
        String sql = "SELECT * FROM loan_statuses WHERE id = ?";
        List<LoanStatus> statuses = jdbcTemplate.query(sql, loanStatusMapper, id);
        return statuses.stream().findFirst();
    }

    @Override
    public Optional<LoanStatus> findByName(String name) {
        String sql = "SELECT * FROM loan_statuses WHERE name = ?";
        List<LoanStatus> statuses = jdbcTemplate.query(sql, loanStatusMapper, name);
        return statuses.stream().findFirst();
    }
}
