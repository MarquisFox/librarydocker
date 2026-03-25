package ru.lab.librarydocker.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class AuthorUpdateRequest {
    @NotBlank
    private String name;

    private LocalDate birthDate;
    private String biography;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}