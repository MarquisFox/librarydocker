package ru.lab.librarydocker.entity;
import java.time.LocalDate;

public class Author {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String biography;

    public Author() {}

    public Author(String name, LocalDate birthDate, String biography) {
        this.name = name;
        this.birthDate = birthDate;
        this.biography = biography;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
}
