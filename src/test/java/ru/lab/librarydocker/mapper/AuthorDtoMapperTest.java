package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import ru.lab.librarydocker.dto.request.AuthorCreateRequest;
import ru.lab.librarydocker.dto.request.AuthorUpdateRequest;
import ru.lab.librarydocker.dto.response.AuthorResponse;
import ru.lab.librarydocker.entity.Author;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorDtoMapperTest {

    @Test
    void toEntity_shouldMapAllFields() {
        AuthorCreateRequest request = new AuthorCreateRequest();
        request.setName("John Doe");
        request.setBirthDate(LocalDate.of(1970, 1, 1));
        request.setBiography("Test biography");

        Author author = AuthorDtoMapper.toEntity(request);

        assertThat(author.getId()).isNull();
        assertThat(author.getName()).isEqualTo("John Doe");
        assertThat(author.getBirthDate()).isEqualTo("1970-01-01");
        assertThat(author.getBiography()).isEqualTo("Test biography");
    }

    @Test
    void updateEntity_shouldUpdateExistingAuthor() {
        Author existing = new Author();
        existing.setName("Old Name");
        existing.setBirthDate(LocalDate.of(1960, 1, 1));
        existing.setBiography("Old bio");

        AuthorUpdateRequest request = new AuthorUpdateRequest();
        request.setName("New Name");
        request.setBirthDate(LocalDate.of(1980, 1, 1));
        request.setBiography("New bio");

        AuthorDtoMapper.updateEntity(existing, request);

        assertThat(existing.getName()).isEqualTo("New Name");
        assertThat(existing.getBirthDate()).isEqualTo("1980-01-01");
        assertThat(existing.getBiography()).isEqualTo("New bio");
    }

    @Test
    void toResponse_shouldMapAllFields() {
        Author author = new Author();
        author.setId(100L);
        author.setName("Jane Smith");
        author.setBirthDate(LocalDate.of(1990, 5, 15));
        author.setBiography("Writer");

        AuthorResponse response = AuthorDtoMapper.toResponse(author);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getName()).isEqualTo("Jane Smith");
        assertThat(response.getBirthDate()).isEqualTo("1990-05-15");
        assertThat(response.getBiography()).isEqualTo("Writer");
    }
}