import repository.userRepository;
import repository.ItemsRepository;
import service.menuService;
import service.userService;
import service.ItemsService;

import java.util.Scanner;

public class app {

    public static void main(String[] args) {
        System.out.println("===== Welcome to the Library System =====");
        Scanner scanner = new Scanner(System.in);
        userRepository userRepository = new userRepository();
        userService userService = new userService(userRepository);
        ItemsRepository itemsRepository = new ItemsRepository();
        ItemsService itemsService = new ItemsService(itemsRepository);
        menuService menuService = new menuService(scanner, userService, itemsService);
        menuService.showMainMenu();
        scanner.close();
    }
}
