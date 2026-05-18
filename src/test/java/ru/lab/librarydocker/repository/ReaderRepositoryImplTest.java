package ru.lab.librarydocker.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.lab.librarydocker.entity.Reader;
import ru.lab.librarydocker.mapper.ReaderMapper;
import ru.lab.librarydocker.repository.BaseRepositoryTest;
import ru.lab.librarydocker.repository.impl.ReaderRepositoryImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({ReaderRepositoryImpl.class, ReaderMapper.class})
class ReaderRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private ReaderRepositoryImpl readerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM readers");
    }

    @Test
    void save_shouldInsertAndReturnGeneratedId() {
        Reader reader = new Reader();
        reader.setFirstName("John");
        reader.setLastName("Doe");
        reader.setEmail("john@example.com");
        reader.setPhone("123456");
        reader.setRegistrationDate(LocalDate.now());

        Reader saved = readerRepository.save(reader);

        assertThat(saved.getId()).isNotNull();
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM readers", Integer.class)).isEqualTo(1);
    }

    @Test
    void save_shouldUpdateExisting() {
        jdbcTemplate.update("INSERT INTO readers (id, first_name, last_name) VALUES (1, 'Old', 'User')");
        Reader reader = new Reader();
        reader.setId(1L);
        reader.setFirstName("Updated");
        reader.setLastName("Name");
        reader.setEmail("updated@example.com");
        reader.setPhone("999");
        reader.setRegistrationDate(LocalDate.now());

        Reader updated = readerRepository.save(reader);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(jdbcTemplate.queryForObject("SELECT first_name FROM readers WHERE id=1", String.class)).isEqualTo("Updated");
    }

    @Test
    void findById_shouldReturnReaderWhenExists() {
        jdbcTemplate.update("INSERT INTO readers (id, first_name, last_name) VALUES (1, 'Find', 'Me')");
        Optional<Reader> found = readerRepository.findById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Find");
    }

    @Test
    void findAll_shouldReturnAllReaders() {
        jdbcTemplate.update("INSERT INTO readers (id, first_name, last_name) VALUES (1, 'A', 'B'), (2, 'C', 'D')");
        List<Reader> readers = readerRepository.findAll();
        assertThat(readers).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveReader() {
        jdbcTemplate.update("INSERT INTO readers (id, first_name, last_name) VALUES (1, 'Delete', 'Me')");
        readerRepository.deleteById(1L);
        assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM readers", Integer.class)).isZero();
    }

    @Test
    void existsById_shouldReturnTrueWhenExists() {
        jdbcTemplate.update("INSERT INTO readers (id, first_name, last_name) VALUES (1, 'Exists', 'Reader')");
        assertThat(readerRepository.existsById(1L)).isTrue();
    }
}