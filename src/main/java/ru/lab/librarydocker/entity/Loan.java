package ru.lab.librarydocker.entity;

import java.time.LocalDate;

public class Loan {
    private Long id;
    private Long bookId;
    private Long readerId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private Long statusId;
    ;

    public Loan() {
    }

    public Loan(Long bookId, Long readerId, LocalDate loanDate, LocalDate dueDate, Long statusId) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.statusId = statusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long status) {
        this.statusId = status;
    }
}
