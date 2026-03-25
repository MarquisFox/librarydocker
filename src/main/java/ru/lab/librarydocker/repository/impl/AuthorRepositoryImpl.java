package ru.lab.librarydocker.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.lab.librarydocker.entity.Author;
import ru.lab.librarydocker.mapper.AuthorMapper;
import ru.lab.librarydocker.repository.AuthorRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AuthorMapper authorMapper;

    public AuthorRepositoryImpl(JdbcTemplate jdbcTemplate, AuthorMapper authorMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT * FROM authors";
        return jdbcTemplate.query(sql, authorMapper);
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";
        List<Author> authors = jdbcTemplate.query(sql, authorMapper, id);
        return authors.stream().findFirst();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            String sql = "INSERT INTO authors (name, birth_date, biography) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, author.getName());
                if (author.getBirthDate() != null) {
                    ps.setDate(2, java.sql.Date.valueOf(author.getBirthDate()));
                } else {
                    ps.setNull(2, java.sql.Types.DATE);
                }
                ps.setString(3, author.getBiography());
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new RuntimeException("Failed to retrieve generated ID for author");
            }
            author.setId(key.longValue());
        } else {
            String sql = "UPDATE authors SET name = ?, birth_date = ?, biography = ? WHERE id = ?";
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, author.getName());
                if (author.getBirthDate() != null) {
                    ps.setDate(2, java.sql.Date.valueOf(author.getBirthDate()));
                } else {
                    ps.setNull(2, java.sql.Types.DATE);
                }
                ps.setString(3, author.getBiography());
                ps.setLong(4, author.getId());
                return ps;
            });
        }
        return author;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM authors WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
