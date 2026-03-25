package ru.lab.librarydocker.service;

import ru.lab.librarydocker.dto.request.ReaderCreateRequest;
import ru.lab.librarydocker.dto.request.ReaderUpdateRequest;
import ru.lab.librarydocker.dto.response.ReaderResponse;

import java.util.List;

public interface ReaderService {
    List<ReaderResponse> getAllReaders();
    ReaderResponse getReaderById(Long id);
    ReaderResponse createReader(ReaderCreateRequest request);
    ReaderResponse updateReader(Long id, ReaderUpdateRequest request);
    void deleteReader(Long id);
}
