package ru.lab.librarydocker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lab.librarydocker.dto.request.AuthorCreateRequest;
import ru.lab.librarydocker.dto.request.AuthorUpdateRequest;
import ru.lab.librarydocker.dto.response.AuthorResponse;
import ru.lab.librarydocker.entity.Author;
import ru.lab.librarydocker.exception.ResourceNotFoundException;
import ru.lab.librarydocker.mapper.AuthorDtoMapper;
import ru.lab.librarydocker.repository.AuthorRepository;
import ru.lab.librarydocker.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(AuthorDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponse getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return AuthorDtoMapper.toResponse(author);
    }

    @Override
    public AuthorResponse createAuthor(AuthorCreateRequest request) {
        Author author = AuthorDtoMapper.toEntity(request);
        Author saved = authorRepository.save(author);
        return AuthorDtoMapper.toResponse(saved);
    }

    @Override
    public AuthorResponse updateAuthor(Long id, AuthorUpdateRequest request) {
        Author existing = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        AuthorDtoMapper.updateEntity(existing, request);
        Author updated = authorRepository.save(existing);
        return AuthorDtoMapper.toResponse(updated);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}
