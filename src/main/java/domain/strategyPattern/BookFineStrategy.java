package domain.strategyPattern;

public class BookFineStrategy implements FineStrategy {
    @Override
    public int calculateFine(int overdueDays) {
        return overdueDays * 10;
    }
}
