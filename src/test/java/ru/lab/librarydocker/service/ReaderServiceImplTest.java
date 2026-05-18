package ru.lab.librarydocker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.lab.librarydocker.dto.request.ReaderCreateRequest;
import ru.lab.librarydocker.dto.request.ReaderUpdateRequest;
import ru.lab.librarydocker.dto.response.ReaderResponse;
import ru.lab.librarydocker.entity.Reader;
import ru.lab.librarydocker.exception.ResourceNotFoundException;
import ru.lab.librarydocker.repository.ReaderRepository;
import ru.lab.librarydocker.service.impl.ReaderServiceImpl;
import ru.lab.librarydocker.utils.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReaderServiceImplTest {

    @Mock
    private ReaderRepository readerRepository;

    @InjectMocks
    private ReaderServiceImpl readerService;

    @Test
    void getAllReaders_shouldReturnList() {
        Reader reader1 = new Reader();
        reader1.setId(1L);
        Reader reader2 = new Reader();
        reader2.setId(2L);
        when(readerRepository.findAll()).thenReturn(List.of(reader1, reader2));

        List<ReaderResponse> result = readerService.getAllReaders();
        assertThat(result).hasSize(2);
    }

    @Test
    void getReaderById_whenExists_shouldReturn() {
        Reader reader = new Reader();
        reader.setId(10L);
        when(readerRepository.findById(10L)).thenReturn(Optional.of(reader));
        ReaderResponse response = readerService.getReaderById(10L);
        assertThat(response.getId()).isEqualTo(10L);
    }

    @Test
    void getReaderById_whenNotFound_shouldThrow() {
        when(readerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> readerService.getReaderById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createReader_shouldValidateEmailAndSave() {
        ReaderCreateRequest request = new ReaderCreateRequest();
        request.setEmail("test@example.com");
        request.setFirstName("John");
        Reader savedReader = new Reader();
        savedReader.setId(5L);
        when(readerRepository.save(any(Reader.class))).thenReturn(savedReader);

        ReaderResponse response = readerService.createReader(request);

        assertThat(response.getId()).isEqualTo(5L);
        verify(readerRepository).save(any(Reader.class));
    }

    @Test
    void createReader_shouldSetRegistrationDateToNowIfNull() {
        ReaderCreateRequest request = new ReaderCreateRequest();
        request.setFirstName("Jane");
        request.setEmail("jane@example.com");
        request.setRegistrationDate(null);

        when(readerRepository.save(any(Reader.class))).thenAnswer(inv -> inv.getArgument(0));

        ReaderResponse response = readerService.createReader(request);
        assertThat(response.getRegistrationDate()).isNotNull();
    }

    @Test
    void updateReader_whenExists_shouldUpdate() {
        Long id = 1L;
        Reader existing = new Reader();
        existing.setId(id);
        ReaderUpdateRequest request = new ReaderUpdateRequest();
        request.setEmail("updated@example.com");
        when(readerRepository.findById(id)).thenReturn(Optional.of(existing));
        when(readerRepository.save(any(Reader.class))).thenReturn(existing);

        ReaderResponse response = readerService.updateReader(id, request);
        assertThat(response.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void updateReader_whenNotFound_shouldThrow() {
        when(readerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> readerService.updateReader(99L, new ReaderUpdateRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteReader_whenExists_shouldDelete() {
        when(readerRepository.existsById(1L)).thenReturn(true);
        readerService.deleteReader(1L);
        verify(readerRepository).deleteById(1L);
    }

    @Test
    void deleteReader_whenNotFound_shouldThrow() {
        when(readerRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> readerService.deleteReader(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(readerRepository, never()).deleteById(any());
    }
}
