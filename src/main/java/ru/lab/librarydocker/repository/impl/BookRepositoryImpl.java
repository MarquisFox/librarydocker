package ru.lab.librarydocker.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.entity.Book;
import ru.lab.librarydocker.mapper.BookMapper;
import ru.lab.librarydocker.mapper.BookWithAuthorMapper;
import ru.lab.librarydocker.repository.BookRepository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;
    private final BookMapper bookMapper;
    private final BookWithAuthorMapper bookWithAuthorMapper;

    public BookRepositoryImpl(JdbcTemplate jdbcTemplate, BookMapper bookMapper, BookWithAuthorMapper bookWithAuthorMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookMapper = bookMapper;
        this.bookWithAuthorMapper = bookWithAuthorMapper;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        return jdbcTemplate.query(sql, bookMapper);
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        List<Book> books = jdbcTemplate.query(sql, bookMapper, id);
        return books.stream().findFirst();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            String sql = "INSERT INTO books (title, author_id, genre, published_year, isbn, available_copies) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // ← указываем столбец
                ps.setString(1, book.getTitle());
                ps.setLong(2, book.getAuthorId());
                ps.setString(3, book.getGenre());
                if (book.getPublishedYear() != null) {
                    ps.setInt(4, book.getPublishedYear());
                } else {
                    ps.setNull(4, java.sql.Types.INTEGER);
                }
                ps.setString(5, book.getIsbn());
                if (book.getAvailableCopies() != null) {
                    ps.setInt(6, book.getAvailableCopies());
                } else {
                    ps.setNull(6, java.sql.Types.INTEGER);
                }
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new RuntimeException("Failed to retrieve generated ID for book");
            }
            book.setId(key.longValue());
        } else {
            String sql = "UPDATE books SET title = ?, author_id = ?, genre = ?, published_year = ?, isbn = ?, available_copies = ? " +
                    "WHERE id = ?";
            jdbcTemplate.update(sql,
                    book.getTitle(), book.getAuthorId(), book.getGenre(),
                    book.getPublishedYear(), book.getIsbn(), book.getAvailableCopies(),
                    book.getId());
        }
        return book;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM books WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public List<BookResponse> findAllWithAuthorNames() {
        String sql = """
                SELECT b.id, b.title, b.author_id, a.name AS author_name,
                       b.genre, b.published_year, b.isbn, b.available_copies
                FROM books b
                JOIN authors a ON b.author_id = a.id
                """;
        return jdbcTemplate.query(sql, bookWithAuthorMapper);
    }

    @Override
    public Optional<BookResponse> findByIdWithAuthorName(Long id) {
        String sql = """
                SELECT b.id, b.title, b.author_id, a.name AS author_name,
                       b.genre, b.published_year, b.isbn, b.available_copies
                FROM books b
                JOIN authors a ON b.author_id = a.id
                WHERE b.id = ?
                """;
        List<BookResponse> results = jdbcTemplate.query(sql, bookWithAuthorMapper, id);
        return results.stream().findFirst();
    }

    @Override
    public List<BookResponse> findByAuthorIdWithAuthorName(Long authorId) {
        String sql = """
                SELECT b.id, b.title, b.author_id, a.name AS author_name,
                       b.genre, b.published_year, b.isbn, b.available_copies
                FROM books b
                JOIN authors a ON b.author_id = a.id
                WHERE b.author_id = ?
                """;
        return jdbcTemplate.query(sql, bookWithAuthorMapper, authorId);
    }

    @Override
    public List<BookResponse> findByGenreWithAuthorName(String genre) {
        String sql = """
                SELECT b.id, b.title, b.author_id, a.name AS author_name,
                       b.genre, b.published_year, b.isbn, b.available_copies
                FROM books b
                JOIN authors a ON b.author_id = a.id
                WHERE b.genre = ?
                """;
        return jdbcTemplate.query(sql, bookWithAuthorMapper, genre);
    }

    @Override
    public List<BookResponse> findByAuthorIdAndGenreWithAuthorName(Long authorId, String genre) {
        String sql = """
                SELECT b.id, b.title, b.author_id, a.name AS author_name,
                       b.genre, b.published_year, b.isbn, b.available_copies
                FROM books b
                JOIN authors a ON b.author_id = a.id
                WHERE b.author_id = ? AND b.genre = ?
                """;
        return jdbcTemplate.query(sql, bookWithAuthorMapper, authorId, genre);
    }
}
