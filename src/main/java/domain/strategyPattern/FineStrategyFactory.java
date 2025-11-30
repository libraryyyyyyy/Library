package domain.strategyPattern;

public class FineStrategyFactory {

    public static FineStrategy getStrategy(String itemType) {
        switch (itemType.toLowerCase()) {
            case "book":
                return new BookFineStrategy();
            case "cd":
                return new CDFineStrategy();
            default:
                throw new IllegalArgumentException("Unknown item type: " + itemType);
        }

    }

    public int calculateFineForItem(String itemType, int overdueDays) {
        FineStrategy strategy = FineStrategyFactory.getStrategy(itemType);
        return strategy.calculateFine(overdueDays);
    }
}
