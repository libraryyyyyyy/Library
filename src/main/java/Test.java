import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println("Hello!! World");
        System.out.println("Hello Shatha");
        System.out.println("testing 9/10/2025 5:58 PM , Hello Sara");

        Scanner input = new Scanner( System.in);
        BookTable Book = new BookTable();
        UserTable User = new UserTable();
        boolean in = false;
        while(true){
            System.out.println("\n1. Login\n2. Add Book\n3. Search Book\n4. Logout\n5. Exit");
            int choice = input.nextInt();
            input.nextLine();


            switch (choice){
                case 1:
                    System.out.print(" Username: ");
                    String user = input.nextLine();
                    System.out.print(" Password: ");
                    String password = input.nextLine();
                     boolean safe = User.login(user , password);
                    if(  safe ){
                        in = true;
                        System.out.println(" You are in now ");
                    }
                    else {
                        System.out.print(" Incorrect Username or Password");
                    }
                    break;
                case 2:
                    if(!in){
                        System.out.println(" Need to log in FIRST !");
                        break;
                    }
                    else{
                        System.out.println(" Add a book :");
                        String name , author , isbn;
                        System.out.print(" Name: ");
                        name = input.nextLine();
                        System.out.print(" Author: ");
                        author= input.nextLine();
                        System.out.print(" ISBN: ");
                        isbn = input.nextLine();
                        book b = new book(name, author, isbn);
                        Book.addBook(b);
                        System.out.println(" Book added successfully");
                        break;
                    }
                case 3:
                    if(!in){
                        System.out.println(" Need to log in FIRST !");
                        break;
                    }
                    else {
                        System.out.println(" Search for the book by( Name / author / ISBN ): ");
                        String keyword = input.nextLine();
                        List<book> res = new ArrayList<>();
                        res = Book.searchBook(keyword);
                        for (book bo : res) System.out.println(bo);
                        break;
                    }
                case 4:
                    if(!in){
                        System.out.println(" Need to log in FIRST !");
                        break;
                    }
                    else{
                        boolean ok =  User.logout(in);
                        in = false;
                        if (ok) System.out.println(" Logged out ");
                        break;
                    }
                case 5:
                    System.out.println(" BYE!");
                    return;


            }
        }
    }
}
