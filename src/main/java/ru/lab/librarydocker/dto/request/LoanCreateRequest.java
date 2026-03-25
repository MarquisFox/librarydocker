package ru.lab.librarydocker.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LoanCreateRequest {
    @NotNull
    private Long bookId;

    @NotNull
    private Long readerId;

    @Min(1)
    private Integer loanDays;

    public @NotNull Long getBookId() {
        return bookId;
    }

    public void setBookId(@NotNull Long bookId) {
        this.bookId = bookId;
    }

    public @NotNull Long getReaderId() {
        return readerId;
    }

    public void setReaderId(@NotNull Long readerId) {
        this.readerId = readerId;
    }

    public @Min(1) Integer getLoanDays() {
        return loanDays;
    }

    public void setLoanDays(@Min(1) Integer loanDays) {
        this.loanDays = loanDays;
    }
}
