package service;

import domain.Items;
import domain.libraryType;
import repository.ItemsRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ItemsService {

    private final ItemsRepository itemsRepository;

    public ItemsService(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public boolean addNewItem(String name,
                              String author,
                              int quantity,
                              libraryType type) {

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Item name cannot be empty.");
        if (author == null || author.isBlank())
            throw new IllegalArgumentException("Author cannot be empty.");
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        if (type == null)
            throw new IllegalArgumentException("Type (BOOK / CD) is required.");

        Items item = new Items(author, name, type, quantity, "");
        return itemsRepository.addItem(item);
    }

    public boolean increaseQuantityByISBN(String isbnInput) {
        if (isbnInput == null || isbnInput.isBlank())
            throw new IllegalArgumentException("ISBN cannot be empty.");

        int isbn;
        try {
            isbn = Integer.parseInt(isbnInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ISBN must be a number.");
        }

        var existing = itemsRepository.findByISBN(isbn);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("No item found with this ISBN.");
        }

        return itemsRepository.increaseQuantity(isbn);
    }

    public List<Items> searchByName(String name, libraryType type) {
        List<Items> result = itemsRepository.findByName(name);
        if (type == null) return result;
        return result.stream()
                .filter(i -> i.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Items> searchByAuthor(String author, libraryType type) {
        List<Items> result = itemsRepository.findByAuthor(author);
        if (type == null) return result;
        return result.stream()
                .filter(i -> i.getType() == type)
                .collect(Collectors.toList());
    }

    public Items searchByISBN(String isbnInput) {
        if (isbnInput == null || isbnInput.isBlank())
            throw new IllegalArgumentException("ISBN cannot be empty.");

        int isbn;
        try {
            isbn = Integer.parseInt(isbnInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ISBN must be a number.");
        }

        return itemsRepository.findByISBN(isbn)
                .orElseThrow(() -> new IllegalArgumentException("No item found with this ISBN."));
    }

    public List<Items> searchBooksByName(String name) {
        return searchByName(name, libraryType.Book);
    }

    public List<Items> searchCDsByName(String name) {
        return searchByName(name, libraryType.CD);
    }

    public List<Items> searchBooksByAuthor(String author) {
        return searchByAuthor(author, libraryType.Book);
    }

    public List<Items> searchCDsByAuthor(String author) {
        return searchByAuthor(author, libraryType.CD);
    }
}
