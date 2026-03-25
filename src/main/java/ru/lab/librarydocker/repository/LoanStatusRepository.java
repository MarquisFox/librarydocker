package ru.lab.librarydocker.repository;

import ru.lab.librarydocker.entity.LoanStatus;

import java.util.List;
import java.util.Optional;

public interface LoanStatusRepository {
    List<LoanStatus> findAll();

    Optional<LoanStatus> findById(Long id);

    Optional<LoanStatus> findByName(String name);
}
