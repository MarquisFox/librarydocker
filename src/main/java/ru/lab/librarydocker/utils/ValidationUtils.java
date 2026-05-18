package ru.lab.librarydocker.utils;


import ru.lab.librarydocker.exception.BusinessException;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void validateIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            return;
        }

        String clean = isbn.replaceAll("[-\\s]", "");
        if (clean.matches("\\d{10}")) {
            return;
        }
        if (clean.matches("978\\d{10}|979\\d{10}")) {
            return;
        }
        throw new BusinessException("Invalid ISBN format. Must be 10 digits or 13 digits starting with 978 or 979.");
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
