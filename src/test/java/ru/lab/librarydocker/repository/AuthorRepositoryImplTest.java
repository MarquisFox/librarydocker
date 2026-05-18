package ru.lab.librarydocker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.lab.librarydocker.entity.Author;
import ru.lab.librarydocker.mapper.AuthorMapper;
import ru.lab.librarydocker.repository.impl.AuthorRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({AuthorRepositoryImpl.class, AuthorMapper.class})
class AuthorRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private AuthorRepositoryImpl authorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM authors");
    }

    @Test
    void save_shouldInsertAndGenerateId() {
        Author author = new Author();
        author.setName("Test Author");
        author.setBirthDate(LocalDate.of(1970, 1, 1));
        author.setBiography("Bio");

        Author saved = authorRepository.save(author);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test Author");
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM authors", Integer.class)).isEqualTo(1);
    }

    @Test
    void save_shouldUpdateExisting() {
        jdbcTemplate.update("INSERT INTO authors (id, name, birth_date, biography) VALUES (1, 'Old', '1970-01-01', 'Old bio')");
        Author author = new Author();
        author.setId(1L);
        author.setName("Updated");
        author.setBirthDate(LocalDate.of(1980, 1, 1));
        author.setBiography("New bio");

        Author updated = authorRepository.save(author);

        assertThat(updated.getName()).isEqualTo("Updated");
        assertThat(jdbcTemplate.queryForObject("SELECT name FROM authors WHERE id=1", String.class)).isEqualTo("Updated");
    }

    @Test
    void findById_shouldReturnAuthorWhenExists() {
        jdbcTemplate.update("INSERT INTO authors (id, name, birth_date, biography) VALUES (1, 'Found', '1990-01-01', 'Bio')");
        Optional<Author> found = authorRepository.findById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Found");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<Author> found = authorRepository.findById(99L);
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllAuthors() {
        jdbcTemplate.update("INSERT INTO authors (id, name) VALUES (1, 'A1'), (2, 'A2')");
        List<Author> authors = authorRepository.findAll();
        assertThat(authors).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveAuthor() {
        jdbcTemplate.update("INSERT INTO authors (id, name) VALUES (1, 'ToDelete')");
        authorRepository.deleteById(1L);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM authors", Integer.class)).isZero();
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        jdbcTemplate.update("INSERT INTO authors (id, name) VALUES (1, 'Exists')");
        assertThat(authorRepository.existsById(1L)).isTrue();
    }

    @Test
    void existsById_shouldReturnFalseWhenNotExists() {
        assertThat(authorRepository.existsById(99L)).isFalse();
    }
}