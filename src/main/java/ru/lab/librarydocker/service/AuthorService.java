package ru.lab.librarydocker.service;

import ru.lab.librarydocker.dto.request.AuthorCreateRequest;
import ru.lab.librarydocker.dto.request.AuthorUpdateRequest;
import ru.lab.librarydocker.dto.response.AuthorResponse;

import java.util.List;

public interface AuthorService {
    List<AuthorResponse> getAllAuthors();
    AuthorResponse getAuthorById(Long id);
    AuthorResponse createAuthor(AuthorCreateRequest request);
    AuthorResponse updateAuthor(Long id, AuthorUpdateRequest request);
    void deleteAuthor(Long id);
}
