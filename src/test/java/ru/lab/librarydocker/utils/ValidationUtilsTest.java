package ru.lab.librarydocker.utils;

import org.junit.jupiter.api.Test;
import ru.lab.librarydocker.exception.BusinessException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationUtilsTest {

    @Test
    void validateIsbn_shouldDoNothing_whenIsbnIsNull() {
        assertThatCode(() -> ValidationUtils.validateIsbn(null))
                .doesNotThrowAnyException();
    }

    @Test
    void validateIsbn_shouldDoNothing_whenIsbnIsBlank() {
        assertThatCode(() -> ValidationUtils.validateIsbn("   "))
                .doesNotThrowAnyException();
    }

    @Test
    void validateIsbn_shouldDoNothing_whenIsbnIsValid10Digits() {
        assertThatCode(() -> ValidationUtils.validateIsbn("1234567890"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateIsbn_shouldDoNothing_whenIsbnIsValid13Digits() {
        assertThatCode(() -> ValidationUtils.validateIsbn("9781234567890"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateIsbn_shouldThrow_whenIsbnHasLetters() {
        assertThatThrownBy(() -> ValidationUtils.validateIsbn("abc123"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid ISBN format");
    }

    @Test
    void validateIsbn_shouldThrow_whenIsbnHasWrongLength() {
        assertThatThrownBy(() -> ValidationUtils.validateIsbn("12345"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid ISBN format");
    }

    @Test
    void validateIsbn_shouldThrow_whenIsbnStartsWithWrongPrefix() {
        // допустимый префикс 978 или 979, 123 не допустим
        assertThatThrownBy(() -> ValidationUtils.validateIsbn("1231234567890"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid ISBN format");
    }

    @Test
    void validateEmail_shouldDoNothing_whenEmailIsNull() {
        assertThatCode(() -> ValidationUtils.validateEmail(null))
                .doesNotThrowAnyException();
    }

    @Test
    void validateEmail_shouldDoNothing_whenEmailIsBlank() {
        assertThatCode(() -> ValidationUtils.validateEmail("   "))
                .doesNotThrowAnyException();
    }

    @Test
    void validateEmail_shouldDoNothing_whenEmailIsValid() {
        assertThatCode(() -> ValidationUtils.validateEmail("user@example.com"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateEmail_shouldDoNothing_whenEmailHasPlus() {
        assertThatCode(() -> ValidationUtils.validateEmail("user+tag@example.com"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateEmail_shouldThrow_whenEmailMissingAtSymbol() {
        assertThatThrownBy(() -> ValidationUtils.validateEmail("userexample.com"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void validateEmail_shouldThrow_whenEmailHasInvalidCharacters() {
        assertThatThrownBy(() -> ValidationUtils.validateEmail("user#@example.com"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void validateEmail_shouldThrow_whenEmailHasNoDomain() {
        assertThatThrownBy(() -> ValidationUtils.validateEmail("user@"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid email format");
    }
}
