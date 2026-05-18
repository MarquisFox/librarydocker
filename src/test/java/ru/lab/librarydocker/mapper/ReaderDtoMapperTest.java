package ru.lab.librarydocker.mapper;

import org.junit.jupiter.api.Test;
import ru.lab.librarydocker.dto.request.ReaderCreateRequest;
import ru.lab.librarydocker.dto.request.ReaderUpdateRequest;
import ru.lab.librarydocker.dto.response.ReaderResponse;
import ru.lab.librarydocker.entity.Reader;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ReaderDtoMapperTest {

    @Test
    void toEntity_shouldMapWithProvidedRegistrationDate() {
        ReaderCreateRequest request = new ReaderCreateRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPhone("123456789");
        request.setRegistrationDate(LocalDate.of(2024, 1, 1));

        Reader reader = ReaderDtoMapper.toEntity(request);

        assertThat(reader.getFirstName()).isEqualTo("John");
        assertThat(reader.getLastName()).isEqualTo("Doe");
        assertThat(reader.getEmail()).isEqualTo("john@example.com");
        assertThat(reader.getPhone()).isEqualTo("123456789");
        assertThat(reader.getRegistrationDate()).isEqualTo("2024-01-01");
    }

    @Test
    void toEntity_shouldSetRegistrationDateToNowIfNull() {
        ReaderCreateRequest request = new ReaderCreateRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setRegistrationDate(null);

        Reader reader = ReaderDtoMapper.toEntity(request);
        assertThat(reader.getRegistrationDate()).isNotNull();
        assertThat(reader.getRegistrationDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void updateEntity_shouldUpdate() {
        Reader existing = new Reader();
        existing.setFirstName("Old");
        existing.setLastName("User");
        existing.setEmail("old@example.com");
        existing.setPhone("000");

        ReaderUpdateRequest request = new ReaderUpdateRequest();
        request.setFirstName("New");
        request.setLastName("Name");
        request.setEmail("new@example.com");
        request.setPhone("111");

        ReaderDtoMapper.updateEntity(existing, request);

        assertThat(existing.getFirstName()).isEqualTo("New");
        assertThat(existing.getLastName()).isEqualTo("Name");
        assertThat(existing.getEmail()).isEqualTo("new@example.com");
        assertThat(existing.getPhone()).isEqualTo("111");
    }

    @Test
    void toResponse_shouldMap() {
        Reader reader = new Reader();
        reader.setId(5L);
        reader.setFirstName("Ivan");
        reader.setLastName("Ivanov");
        reader.setEmail("ivan@example.com");
        reader.setPhone("+79991234567");
        reader.setRegistrationDate(LocalDate.of(2025, 3, 20));

        ReaderResponse response = ReaderDtoMapper.toResponse(reader);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getFirstName()).isEqualTo("Ivan");
        assertThat(response.getLastName()).isEqualTo("Ivanov");
        assertThat(response.getEmail()).isEqualTo("ivan@example.com");
        assertThat(response.getPhone()).isEqualTo("+79991234567");
        assertThat(response.getRegistrationDate()).isEqualTo("2025-03-20");
    }
}