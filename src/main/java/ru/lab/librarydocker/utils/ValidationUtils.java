package ru.lab.librarydocker.utils;


import ru.lab.librarydocker.exception.BusinessException;

public final class ValidationUtils {

    private ValidationUtils() {}

    public static void validateIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return;
        }
        String isbnRegex = "^(97[8-9])?[0-9]{9,13}$";
        if (!isbn.matches(isbnRegex)) {
            throw new BusinessException("Invalid ISBN format. Must be 10 or 13 digits, optionally starting with 978 or 979.");
        }
    }


    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailRegex)) {
            throw new BusinessException("Invalid email format");
        }
    }


}
