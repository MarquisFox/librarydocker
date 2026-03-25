package ru.lab.librarydocker.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.lab.librarydocker.entity.Reader;
import ru.lab.librarydocker.mapper.ReaderMapper;
import ru.lab.librarydocker.repository.ReaderRepository;


import java.util.List;
import java.util.Optional;

@Repository
public class ReaderRepositoryImpl implements ReaderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ReaderMapper readerMapper;

    public ReaderRepositoryImpl(JdbcTemplate jdbcTemplate, ReaderMapper readerMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.readerMapper = readerMapper;
    }

    @Override
    public List<Reader> findAll() {
        String sql = "SELECT * FROM readers";
        return jdbcTemplate.query(sql, readerMapper);
    }

    @Override
    public Optional<Reader> findById(Long id) {
        String sql = "SELECT * FROM readers WHERE id = ?";
        List<Reader> readers = jdbcTemplate.query(sql, readerMapper, id);
        return readers.stream().findFirst();
    }

    @Override
    public Reader save(Reader reader) {
        if (reader.getId() == null) {
            String sql = "INSERT INTO readers (first_name, last_name, email, phone, registration_date) VALUES (?, ?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    reader.getFirstName(),
                    reader.getLastName(),
                    reader.getEmail(),
                    reader.getPhone(),
                    reader.getRegistrationDate());
            reader.setId(id);
        } else {
            String sql = "UPDATE readers SET first_name = ?, last_name = ?, email = ?, phone = ?, registration_date = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    reader.getFirstName(),
                    reader.getLastName(),
                    reader.getEmail(),
                    reader.getPhone(),
                    reader.getRegistrationDate(),
                    reader.getId());
        }
        return reader;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM readers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM readers WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
