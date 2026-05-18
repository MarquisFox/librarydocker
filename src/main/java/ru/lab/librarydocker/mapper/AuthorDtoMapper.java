package ru.lab.librarydocker.mapper;

import ru.lab.librarydocker.dto.request.AuthorCreateRequest;
import ru.lab.librarydocker.dto.request.AuthorUpdateRequest;
import ru.lab.librarydocker.dto.response.AuthorResponse;
import ru.lab.librarydocker.entity.Author;

public final class AuthorDtoMapper {

    private AuthorDtoMapper() {
    }

    public static Author toEntity(AuthorCreateRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setBirthDate(request.getBirthDate());
        author.setBiography(request.getBiography());
        return author;
    }

    public static void updateEntity(Author existing, AuthorUpdateRequest request) {
        existing.setName(request.getName());
        existing.setBirthDate(request.getBirthDate());
        existing.setBiography(request.getBiography());
    }

    public static AuthorResponse toResponse(Author author) {
        AuthorResponse response = new AuthorResponse();
        response.setId(author.getId());
        response.setName(author.getName());
        response.setBirthDate(author.getBirthDate());
        response.setBiography(author.getBiography());
        return response;
    }
}
