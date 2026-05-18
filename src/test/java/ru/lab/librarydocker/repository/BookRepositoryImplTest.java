package ru.lab.librarydocker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.lab.librarydocker.dto.response.BookResponse;
import ru.lab.librarydocker.entity.Book;
import ru.lab.librarydocker.mapper.BookMapper;
import ru.lab.librarydocker.mapper.BookWithAuthorMapper;
import ru.lab.librarydocker.repository.impl.BookRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({BookRepositoryImpl.class, BookMapper.class, BookWithAuthorMapper.class})
class BookRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private BookRepositoryImpl bookRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM books");
        jdbcTemplate.execute("DELETE FROM authors");
        jdbcTemplate.update("INSERT INTO authors (id, name) VALUES (1, 'Author1'), (2, 'Author2')");
    }

    @Test
    void save_shouldInsertAndGenerateId() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthorId(1L);
        book.setGenre("Fiction");
        book.setPublishedYear(2020);
        book.setIsbn("1234567890");
        book.setAvailableCopies(5);

        Book saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Book");
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM books", Integer.class)).isEqualTo(1);
    }

    @Test
    void save_shouldUpdateExisting() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'Old', 1)");
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Updated");
        book.setAuthorId(1L);
        book.setGenre("Novel");
        book.setPublishedYear(2021);
        book.setIsbn("9781234567890");
        book.setAvailableCopies(3);

        Book updated = bookRepository.save(book);

        assertThat(updated.getTitle()).isEqualTo("Updated");
        assertThat(jdbcTemplate.queryForObject("SELECT title FROM books WHERE id=1", String.class)).isEqualTo("Updated");
    }

    @Test
    void findById_shouldReturnBookWhenExists() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'Found', 1)");
        Optional<Book> found = bookRepository.findById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Found");
    }

    @Test
    void findAll_shouldReturnAllBooks() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'B1', 1), (2, 'B2', 2)");
        List<Book> books = bookRepository.findAll();
        assertThat(books).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveBook() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'ToDelete', 1)");
        bookRepository.deleteById(1L);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM books", Integer.class)).isZero();
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'Exists', 1)");
        assertThat(bookRepository.existsById(1L)).isTrue();
    }

    // JOIN тесты
    @Test
    void findAllWithAuthorNames_shouldReturnBooksWithAuthorName() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'Book1', 1), (2, 'Book2', 2)");
        List<BookResponse> responses = bookRepository.findAllWithAuthorNames();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getAuthorName()).isEqualTo("Author1");
        assertThat(responses.get(1).getAuthorName()).isEqualTo("Author2");
    }

    @Test
    void findByIdWithAuthorName_shouldReturnBookWithAuthor() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'Specific', 1)");
        Optional<BookResponse> response = bookRepository.findByIdWithAuthorName(1L);
        assertThat(response).isPresent();
        assertThat(response.get().getTitle()).isEqualTo("Specific");
        assertThat(response.get().getAuthorName()).isEqualTo("Author1");
    }

    @Test
    void findByAuthorIdWithAuthorName_shouldReturnBooksByAuthor() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id) VALUES (1, 'Book1', 1), (2, 'Book2', 1)");
        List<BookResponse> responses = bookRepository.findByAuthorIdWithAuthorName(1L);
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getAuthorName()).isEqualTo("Author1");
    }

    @Test
    void findByGenreWithAuthorName_shouldReturnBooksByGenre() {
        jdbcTemplate.update("INSERT INTO books (id, title, genre, author_id) VALUES (1, 'Book1', 'SciFi', 1)");
        List<BookResponse> responses = bookRepository.findByGenreWithAuthorName("SciFi");
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getGenre()).isEqualTo("SciFi");
    }

    @Test
    void findByAuthorIdAndGenreWithAuthorName_shouldReturnFiltered() {
        jdbcTemplate.update("INSERT INTO books (id, title, author_id, genre) VALUES (1, 'Book1', 1, 'Drama'), (2, 'Book2', 1, 'Comedy')");
        List<BookResponse> responses = bookRepository.findByAuthorIdAndGenreWithAuthorName(1L, "Drama");
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getTitle()).isEqualTo("Book1");
    }
}
