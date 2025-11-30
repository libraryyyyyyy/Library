package service;

import domain.*;
import domain.strategyPattern.FineStrategy;
import domain.strategyPattern.FineStrategyFactory;
import repository.BorrowRepository;
import repository.ItemsRepository;

import java.time.LocalDate;
import java.util.List;

public class BorrowService {

    private final BorrowRepository borrowRepo;
    private final ItemsRepository itemsRepo;

    public BorrowService(BorrowRepository borrowRepo, ItemsRepository itemsRepo) {
        this.borrowRepo = borrowRepo;
        this.itemsRepo = itemsRepo;
    }

    public boolean borrowItem(String studentEmail, int isbn) {

        Items item = itemsRepo.findByISBN(isbn)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        LocalDate today = LocalDate.now();
        LocalDate overdue = item.getType() == libraryType.Book ?
                today.plusDays(28) : today.plusDays(7);

        Borrow borrow = new Borrow(studentEmail, isbn, today, overdue, false);
        return borrowRepo.borrowItem(borrow);
    }


    public List<Borrow> getOverdueStudents() {
        return borrowRepo.getOverdueUsers();
    }



}
