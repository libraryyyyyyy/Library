import repository.BorrowRepository;
import repository.userRepository;
import repository.ItemsRepository;
import service.*;
import config.config;

import java.util.Scanner;

public class app {

    public static void main(String[] args) {
        System.out.println("===== Welcome to the Library System =====");

        Scanner scanner = new Scanner(System.in);

        EmailService emailService = new EmailService(
                config.EMAIL,
                config.EMAIL_PASSWORD
        );
        userRepository userRepository = new userRepository();
        userService userService = new userService(userRepository, emailService);
        ItemsRepository itemsRepository = new ItemsRepository();
        ItemsService itemsService = new ItemsService(itemsRepository);
        BorrowRepository borrowRepository = new BorrowRepository();
        BorrowService borrowService = new BorrowService(borrowRepository , itemsRepository);
        menuService menuService = new menuService(scanner, userService, itemsService , borrowService );
        menuService.showMainMenu();

        scanner.close();
    }
}
