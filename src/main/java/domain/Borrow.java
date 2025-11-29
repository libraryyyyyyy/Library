package domain;

import java.time.LocalDate;

public class Borrow {

    private String studentEmail;
    private int isbn;
    private LocalDate borrowDate;
    private LocalDate overdueDate;
    private boolean returned;

    public Borrow(String studentEmail, int isbn, LocalDate borrowDate, LocalDate overdueDate, boolean returned) {
        this.studentEmail = studentEmail;
        this.isbn = isbn;
        this.borrowDate = borrowDate;
        this.overdueDate = overdueDate;
        this.returned = returned;
    }

    public String getStudentEmail() { return studentEmail; }
    public int getIsbn() { return isbn; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getOverdueDate() { return overdueDate; }
    public boolean isReturned() { return returned; }
}
