package ru.lab.librarydocker.unit.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.lab.librarydocker.exception.BusinessException;
import ru.lab.librarydocker.utils.ValidationUtils;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationUtils tests")
class ValidationUtilsTest {


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("validateIsbn should not throw for null or blank ISBN")
    void validateIsbn_nullOrBlank_doesNotThrow(String isbn) {
        assertDoesNotThrow(() -> ValidationUtils.validateIsbn(isbn));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567890",
            "1234567890123",
            "9781234567890",
            "9791234567890",
            "978123456789",
            "97912345678"
    })
    @DisplayName("validateIsbn should not throw for valid ISBN formats")
    void validateIsbn_valid_doesNotThrow(String isbn) {
        assertDoesNotThrow(() -> ValidationUtils.validateIsbn(isbn));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789",
            "12345678901234",
            "97812345678",
            "9791234567",
            "1234567890a",
            "978-1234567890",
            "ISBN1234567890",
            "9781234567890123"
    })
    @DisplayName("validateIsbn should throw BusinessException for invalid ISBN")
    void validateIsbn_invalid_throwsBusinessException(String isbn) {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> ValidationUtils.validateIsbn(isbn)
        );
        assertTrue(exception.getMessage().contains("Invalid ISBN format"));
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("validateEmail should not throw for null or blank email")
    void validateEmail_nullOrBlank_doesNotThrow(String email) {
        assertDoesNotThrow(() -> ValidationUtils.validateEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "user@example.com",
            "first.last@sub.domain.co",
            "user+tag@example.org",
            "user_name@example.net",
            "123@test.com",
            "user@localhost",
            "user@domain-with-dash.com"
    })
    @DisplayName("validateEmail should not throw for valid email formats")
    void validateEmail_valid_doesNotThrow(String email) {
        assertDoesNotThrow(() -> ValidationUtils.validateEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "plainaddress",
            "@missingusername.com",
            "username@.com",
            "username@domain..com",
            "username@domain.c",
            "username@domain",
            "user@",
            "@domain.com",
            "user name@domain.com",
            "user@domain..com",
            "user@domain.c om",
            "user@domain,com",
            "user@domain\\.com"
    })
    @DisplayName("validateEmail should throw BusinessException for invalid email formats")
    void validateEmail_invalid_throwsBusinessException(String email) {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> ValidationUtils.validateEmail(email)
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    @DisplayName("validateEmail rejects obvious invalid patterns")
    void validateEmail_obviousInvalid_throws() {
        assertThrows(BusinessException.class, () -> ValidationUtils.validateEmail("user@domain"));
        assertThrows(BusinessException.class, () -> ValidationUtils.validateEmail("user@"));
        assertThrows(BusinessException.class, () -> ValidationUtils.validateEmail("@domain.com"));
        assertThrows(BusinessException.class, () -> ValidationUtils.validateEmail("user name@domain.com"));
        assertThrows(BusinessException.class, () -> ValidationUtils.validateEmail("user@domain.c om"));
    }
}
