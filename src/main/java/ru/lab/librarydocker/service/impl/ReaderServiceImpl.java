package ru.lab.librarydocker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lab.librarydocker.dto.request.ReaderCreateRequest;
import ru.lab.librarydocker.dto.request.ReaderUpdateRequest;
import ru.lab.librarydocker.dto.response.ReaderResponse;
import ru.lab.librarydocker.entity.Reader;
import ru.lab.librarydocker.exception.ResourceNotFoundException;
import ru.lab.librarydocker.mapper.ReaderDtoMapper;
import ru.lab.librarydocker.repository.ReaderRepository;
import ru.lab.librarydocker.service.ReaderService;
import ru.lab.librarydocker.utils.ValidationUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    public ReaderServiceImpl(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReaderResponse> getAllReaders() {
        return readerRepository.findAll().stream()
                .map(ReaderDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReaderResponse getReaderById(Long id) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with id: " + id));
        return ReaderDtoMapper.toResponse(reader);
    }

    @Override
    public ReaderResponse createReader(ReaderCreateRequest request) {
        ValidationUtils.validateEmail(request.getEmail());
        Reader reader = ReaderDtoMapper.toEntity(request);
        if (reader.getRegistrationDate() == null) {
            reader.setRegistrationDate(LocalDate.now());
        }
        Reader saved = readerRepository.save(reader);
        return ReaderDtoMapper.toResponse(saved);
    }

    @Override
    public ReaderResponse updateReader(Long id, ReaderUpdateRequest request) {
        Reader existing = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with id: " + id));
        ValidationUtils.validateEmail(request.getEmail());
        ReaderDtoMapper.updateEntity(existing, request);
        Reader updated = readerRepository.save(existing);
        return ReaderDtoMapper.toResponse(updated);
    }

    @Override
    public void deleteReader(Long id) {
        if (!readerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reader not found with id: " + id);
        }
        readerRepository.deleteById(id);
    }
}
