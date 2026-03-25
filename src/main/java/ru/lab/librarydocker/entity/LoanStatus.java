package ru.lab.librarydocker.entity;

public class LoanStatus {
    private Long id;
    private String name;

    public LoanStatus() {
    }

    public LoanStatus(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
