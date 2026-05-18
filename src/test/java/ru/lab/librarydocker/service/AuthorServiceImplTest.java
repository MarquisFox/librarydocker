package ru.lab.librarydocker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.dto.request.AuthorCreateRequest;
import ru.lab.librarydocker.dto.request.AuthorUpdateRequest;
import ru.lab.librarydocker.dto.response.AuthorResponse;
import ru.lab.librarydocker.entity.Author;
import ru.lab.librarydocker.exception.ResourceNotFoundException;
import ru.lab.librarydocker.repository.AuthorRepository;
import ru.lab.librarydocker.service.impl.AuthorServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    void getAllAuthors_shouldReturnListOfResponses() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author1");
        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Author2");

        when(authorRepository.findAll()).thenReturn(List.of(author1, author2));

        List<AuthorResponse> result = authorService.getAllAuthors();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Author1");
        assertThat(result.get(1).getName()).isEqualTo("Author2");
        verify(authorRepository).findAll();
    }

    @Test
    void getAuthorById_whenExists_shouldReturnResponse() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Leo Tolstoy");
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorResponse response = authorService.getAuthorById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Leo Tolstoy");
        verify(authorRepository).findById(1L);
    }

    @Test
    void getAuthorById_whenNotFound_shouldThrowResourceNotFoundException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getAuthorById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Author not found with id: 99");
    }

    @Test
    void createAuthor_shouldSaveAndReturnResponse() {
        AuthorCreateRequest request = new AuthorCreateRequest();
        request.setName("New Author");
        request.setBirthDate(LocalDate.of(2000, 1, 1));

        Author savedAuthor = new Author();
        savedAuthor.setId(10L);
        savedAuthor.setName("New Author");
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        AuthorResponse response = authorService.createAuthor(request);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("New Author");
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void updateAuthor_whenExists_shouldUpdateAndReturn() {
        Long id = 1L;
        Author existing = new Author();
        existing.setId(id);
        existing.setName("Old Name");

        AuthorUpdateRequest request = new AuthorUpdateRequest();
        request.setName("Updated Name");

        when(authorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.save(any(Author.class))).thenAnswer(inv -> inv.getArgument(0));

        AuthorResponse response = authorService.updateAuthor(id, request);

        assertThat(response.getName()).isEqualTo("Updated Name");
        verify(authorRepository).save(existing);
    }

    @Test
    void updateAuthor_whenNotFound_shouldThrow() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.updateAuthor(99L, new AuthorUpdateRequest()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Author not found with id: 99");
    }

    @Test
    void deleteAuthor_whenExists_shouldDelete() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        authorService.deleteAuthor(1L);
        verify(authorRepository).deleteById(1L);
    }

    @Test
    void deleteAuthor_whenNotFound_shouldThrow() {
        when(authorRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> authorService.deleteAuthor(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(authorRepository, never()).deleteById(any());
    }
}