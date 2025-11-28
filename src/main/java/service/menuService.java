package service;

import domain.Role;
import domain.user;
import domain.libraryType;
import domain.Items;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

public class menuService {
    private final Scanner scanner;
    private final userService userService;
    private final ItemsService itemsService;

    public menuService(Scanner scanner, userService userService, ItemsService itemsService) {
        this.scanner = scanner;
        this.userService = userService;
        this.itemsService = itemsService;
    }

    public void showMainMenu() {
        System.out.println("===== Welcome to the Library System =====");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Sign Up");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleSignUp();
                case "2" -> handleLogin();
                case "3" -> {
                    System.out.println("👋 Exiting... Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid option. Please try again.");
            }
        }
    }

    private void handleSignUp() {
        System.out.println("\n--- Sign Up ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        String password = readPasswordHidden("Enter password (min 8 chars): ");
        String confirm = readPasswordHidden("Confirm password: ");

        try {
            boolean success = userService.registerUser(email, password, confirm);
            if (success) System.out.println("✅ User registered successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void handleLogin() {
        System.out.println("\n--- Log In ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        String password = readPasswordHidden("Enter password: ");

        try {
            user user = userService.authenticate(email, password);
            System.out.println("\n✅ Login successful! Welcome ");
            showRoleBasedMenu(user);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void showRoleBasedMenu(user user) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n===== " + user.getRole() + " Interface =====");
            switch (user.getRole()) {
                case ADMIN -> loggedIn = showAdminMenu();
                case LIBRARIAN -> loggedIn = showLibrarianMenu();
                case STUDENT -> loggedIn = showStudentMenu();
                default -> {
                    System.out.println("❌ Unknown role.");
                    loggedIn = false;
                }
            }
        }
    }

    private boolean showAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. See Inactive Accounts");
        System.out.println("2. Delete Inactive Account");
        System.out.println("3. Change User's Role");
        System.out.println("4. Add Book / CD");
        System.out.println("5. Logout");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> showInactiveAccounts();
            case "2" -> deleteInactiveAccount();
            case "3" -> changeUserRole();
            case "4" -> handleAddItem();
            case "5" -> {
                System.out.println("🚪 Logging out...");
                return false;
            }
            default -> System.out.println("❌ Invalid choice.");
        }
        return true;
    }

    private void showInactiveAccounts() {
        System.out.println("\n📋 Inactive Accounts:");
        List<user> inactiveUsers = userService.getInactiveUsers();
        if (inactiveUsers.isEmpty()) {
            System.out.println("✅ No inactive users found.");
        } else {
            inactiveUsers.forEach(u ->
                    System.out.println("• " + u.getEmail() + " | Role: " + u.getRole())
            );
        }
    }

    private void deleteInactiveAccount() {
        System.out.print("\nEnter email of inactive account to delete: ");
        String email = scanner.nextLine().trim();

        boolean success = userService.removeInactiveUser(email);
        if (success)
            System.out.println("🗑️ Account deleted successfully.");
        else
            System.out.println("❌ Account not found or not inactive.");
    }

    private void changeUserRole() {
        System.out.print("\nEnter user email: ");
        String email = scanner.nextLine().trim();

        System.out.println("Choose new role:");
        System.out.println("1. ADMIN");
        System.out.println("2. LIBRARIAN");
        System.out.println("3. STUDENT");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();

        Role newRole = switch (choice) {
            case "1" -> Role.ADMIN;
            case "2" -> Role.LIBRARIAN;
            case "3" -> Role.STUDENT;
            default -> null;
        };

        if (newRole == null) {
            System.out.println("❌ Invalid role choice.");
            return;
        }

        boolean updated = userService.updateUserRole(email, newRole);
        if (updated)
            System.out.println("✅ Role updated successfully.");
        else
            System.out.println("❌ User not found.");
    }

    private boolean showLibrarianMenu() {
        System.out.println("\n--- Librarian Menu ---");
        System.out.println("1. See Overdue users");
        System.out.println("2. Logout");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> System.out.println("📨 [Librarian] See Overdue users (not implemented yet).");
            case "2" -> {
                System.out.println("🚪 Logging out...");
                return false;
            }
            default -> System.out.println("❌ Invalid choice.");
        }
        return true;
    }

    private boolean showStudentMenu() {
        System.out.println("\n--- Student Menu ---");
        System.out.println("1. Search Book");
        System.out.println("2. Search CD");
        System.out.println("3. Logout");
        System.out.print("Choose option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> handleSearchBook();
            case "2" -> handleSearchCD();
            case "3" -> {
                System.out.println("🚪 Logging out...");
                return false;
            }
            default -> System.out.println("❌ Invalid choice.");
        }
        return true;
    }

    private void handleAddItem() {
        System.out.println("\n--- Add Item ---");

        System.out.println("Choose type:");
        System.out.println("1. BOOK");
        System.out.println("2. CD");
        System.out.print("Enter choice: ");
        String t = scanner.nextLine().trim();

        libraryType type = switch (t) {
            case "1" -> libraryType.Book;
            case "2" -> libraryType.CD;
            default -> null;
        };

        if (type == null) {
            System.out.println("❌ Invalid type.");
            return;
        }

        System.out.println("\nIs this item:");
        System.out.println("1. Existing (increase quantity)");
        System.out.println("2. New item");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        try {
            if ("1".equals(choice)) {
                System.out.print("Enter ISBN of existing item: ");
                String isbn = scanner.nextLine().trim();

                // ⬇⬇⬇ التعديل المهم هون ⬇⬇⬇
                // أولاً نجيب الـ item من السيرفس
                Items item = itemsService.searchByISBN(isbn); // لو مش موجود، بترمي IllegalArgumentException

                // نتأكد إن نوعه نفس اللي اختارته (BOOK أو CD)
                if (item.getType() != type) {
                    System.out.println("❌ This ISBN belongs to a " + item.getType() + " not a " + type + ".");
                    return;
                }

                // إذا النوع صح → نزيد الكمية
                boolean ok = itemsService.increaseQuantityByISBN(isbn);
                if (ok)
                    System.out.println("✅ Quantity increased by 1.");
                else
                    System.out.println("❌ Failed to increase quantity.");

            } else if ("2".equals(choice)) {
                System.out.print("Enter name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Enter author: ");
                String author = scanner.nextLine().trim();

                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());

                boolean ok = itemsService.addNewItem(name, author, quantity, type);
                if (ok)
                    System.out.println("✅ Item added successfully.");
                else
                    System.out.println("❌ Failed to add item.");

            } else {
                System.out.println("❌ Invalid choice.");
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Quantity must be a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void handleSearchBook() {
        System.out.println("\n--- Search Book ---");
        System.out.println("1. Search by name");
        System.out.println("2. Search by author");
        System.out.println("3. Search by ISBN");
        System.out.println("4. Back");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter book name (or part of it): ");
                    String name = scanner.nextLine().trim();
                    List<Items> items = itemsService.searchBooksByName(name);
                    printItems(items, libraryType.Book);
                }
                case "2" -> {
                    System.out.print("Enter author name (or part of it): ");
                    String author = scanner.nextLine().trim();
                    List<Items> items = itemsService.searchBooksByAuthor(author);
                    printItems(items, libraryType.Book);
                }
                case "3" -> {
                    System.out.print("Enter ISBN (number): ");
                    String isbn = scanner.nextLine().trim();
                    Items item = itemsService.searchByISBN(isbn);
                    if (item.getType() == libraryType.Book) {
                        printSingleItem(item);
                    } else {
                        System.out.println("❌ This ISBN belongs to a CD, not a Book.");
                    }
                }
                case "4" -> {
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void handleSearchCD() {
        System.out.println("\n--- Search CD ---");
        System.out.println("1. Search by name");
        System.out.println("2. Search by author");
        System.out.println("3. Search by ISBN");
        System.out.println("4. Back");
        System.out.print("Enter choice: ");

        String choice = scanner.nextLine().trim();

        try {
            switch (choice) {
                case "1" -> {
                    System.out.print("Enter CD name (or part of it): ");
                    String name = scanner.nextLine().trim();
                    List<Items> items = itemsService.searchCDsByName(name);
                    printItems(items, libraryType.CD);
                }
                case "2" -> {
                    System.out.print("Enter artist/author (or part of it): ");
                    String author = scanner.nextLine().trim();
                    List<Items> items = itemsService.searchCDsByAuthor(author);
                    printItems(items, libraryType.CD);
                }
                case "3" -> {
                    System.out.print("Enter ISBN (number): ");
                    String isbn = scanner.nextLine().trim();
                    Items item = itemsService.searchByISBN(isbn);
                    if (item.getType() == libraryType.CD) {
                        printSingleItem(item);
                    } else {
                        System.out.println("❌ This ISBN belongs to a Book, not a CD.");
                    }
                }
                case "4" -> {
                    return;
                }
                default -> System.out.println("❌ Invalid choice.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private void printItems(List<Items> items, libraryType expectedType) {
        if (items.isEmpty()) {
            System.out.println("⚠️ No items found.");
            return;
        }

        System.out.println("\nResults:");
        for (Items item : items) {
            if (item.getType() != expectedType) continue;
            printSingleItem(item);
        }
    }

    private void printSingleItem(Items item) {
        System.out.println("------------------------------------");
        System.out.println("ISBN:     " + item.getISBN());
        System.out.println("Name:     " + item.getName());
        System.out.println("Author:   " + item.getAuthor());
        System.out.println("Type:     " + item.getType());
        System.out.println("Quantity: " + item.getQuantity());
    }

    private String readPasswordHidden(String prompt) {
        Console console = System.console();
        if (console != null) {
            char[] passArray = console.readPassword(prompt);
            return new String(passArray);
        } else {
            System.out.print(prompt + " ");
            return scanner.nextLine();
        }
    }
}
