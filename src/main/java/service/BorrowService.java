package service;

import domain.Borrow;
import domain.Items;
import domain.libraryType;
import repository.BorrowRepository;
import repository.ItemsRepository;
import domain.strategyPattern.FineStrategyFactory;  // your factory
import domain.strategyPattern.FineStrategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BorrowService {

    private final BorrowRepository borrowRepo;
    private final ItemsRepository itemsRepo;

    public BorrowService(BorrowRepository borrowRepo, ItemsRepository itemsRepo) {
        this.borrowRepo = borrowRepo;
        this.itemsRepo = itemsRepo;
    }

    public boolean hasUnpaidFine(String email) {
        return borrowRepo.getTotalFine(email) > 0;
    }

    public int getTotalFine(String email) {
        return borrowRepo.getTotalFine(email);
    }

    public void payFine(String email, int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Payment must be positive.");
        borrowRepo.updateFineAfterPayment(email, amount);
    }

    public boolean borrowItem(String studentEmail, int isbn) {

        if (hasUnpaidFine(studentEmail)) {
            throw new IllegalArgumentException("You have unpaid fines. Pay before borrowing.");
        }

        Items item = itemsRepo.findByISBN(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        LocalDate today = LocalDate.now();
        LocalDate overdue = item.getType() == libraryType.Book ?
                today.plusDays(28) : today.plusDays(7);

        Borrow borrow = new Borrow(studentEmail, isbn, today, overdue, false);
        return borrowRepo.borrowItem(borrow);
    }

    // return workflow: checks unpaid previous fines, computes fine for this returned item and marks returned
    public boolean returnItem(String studentEmail, int isbn) {

        if (hasUnpaidFine(studentEmail)) {
            throw new IllegalArgumentException("You have unpaid fines. Pay before returning items.");
        }

        // find active borrow
        Borrow active = borrowRepo.findActiveBorrow(studentEmail, isbn);
        if (active == null) {
            throw new IllegalArgumentException("No active borrow found for this ISBN and student.");
        }

        // compute overdueDays: if overdue_date < today then days overdue = days between overdue_date and today
        LocalDate today = LocalDate.now();
        long overdueDays = 0;
        if (active.getOverdueDate() != null && active.getOverdueDate().isBefore(today)) {
            overdueDays = ChronoUnit.DAYS.between(active.getOverdueDate(), today);
        }

        // get item type to choose fine strategy
        Items item = itemsRepo.findByISBN(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Item not found when returning"));

        // choose strategy
        FineStrategy strategy = FineStrategyFactory.getStrategy(item.getType().name());
        int fine = strategy.calculateFine((int) overdueDays);

        // mark returned and set the fine on that borrow row
        boolean updated = borrowRepo.markReturnedByStudentAndIsbn(studentEmail, isbn, fine);

        // increase quantity back
        if (updated) {
            itemsRepo.increaseQuantity(isbn); // you already have increaseQuantity(int isbn)
        }

        return updated;
    }

    public List<Borrow> getOverdueStudents() {
        return borrowRepo.getOverdueUsers();
    }

    public List<String> getStudentsWithUnpaidFines() {
        return borrowRepo.getStudentsWithUnpaidFines();
    }

}
