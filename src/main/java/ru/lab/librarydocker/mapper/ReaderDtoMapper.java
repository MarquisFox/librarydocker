package ru.lab.librarydocker.mapper;

import ru.lab.librarydocker.dto.request.ReaderCreateRequest;
import ru.lab.librarydocker.dto.request.ReaderUpdateRequest;
import ru.lab.librarydocker.dto.response.ReaderResponse;
import ru.lab.librarydocker.entity.Reader;

import java.time.LocalDate;

public final class ReaderDtoMapper {

    private ReaderDtoMapper() {
    }

    public static Reader toEntity(ReaderCreateRequest request) {
        Reader reader = new Reader();
        reader.setFirstName(request.getFirstName());
        reader.setLastName(request.getLastName());
        reader.setEmail(request.getEmail());
        reader.setPhone(request.getPhone());
        reader.setRegistrationDate(request.getRegistrationDate() != null ? request.getRegistrationDate() : LocalDate.now());
        return reader;
    }

    public static void updateEntity(Reader existing, ReaderUpdateRequest request) {
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());
    }

    public static ReaderResponse toResponse(Reader reader) {
        ReaderResponse response = new ReaderResponse();
        response.setId(reader.getId());
        response.setFirstName(reader.getFirstName());
        response.setLastName(reader.getLastName());
        response.setEmail(reader.getEmail());
        response.setPhone(reader.getPhone());
        response.setRegistrationDate(reader.getRegistrationDate());
        return response;
    }
}
