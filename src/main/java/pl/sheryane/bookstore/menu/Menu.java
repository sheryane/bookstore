package pl.sheryane.bookstore.menu;

import pl.sheryane.bookstore.model.Book;
import pl.sheryane.bookstore.model.Category;
import pl.sheryane.bookstore.repository.BookRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Menu implements Runnable {
    private String menu;
    private Scanner scanner;
    private BookRepository repository;

    public Menu() {
        this.menu = ("Welcome in the Bookstore Management System, what do you want to do? \n" +
                "1. Add a book. \n" +
                "2. Modify a book. \n" +
                "3. Show a book. \n" +
                "4. Remove a book. \n" +
                "5. EXIT");
        this.scanner = new Scanner(System.in);
        this.repository = new BookRepository();
    }

    private void newBook(Book book) throws ParseException {
        title(book);
        date(book);
        isbn(book);
        category(book);
        pages(book);
        description(book);
        price(book);
        repository.create(book);
    }

    private String printMenu() {
        System.out.println(menu);
        return scanner.nextLine();
    }

    private void title(Book book) {
        System.out.println("Enter a title:");
        book.setTitle(scanner.nextLine());
    }

    private void date(Book book) throws ParseException {
        System.out.println("Enter release date (DD/MM/YYYY):");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(scanner.nextLine());
        book.setReleaseDate(date);
    }

    private void isbn(Book book) {
        System.out.println("Enter ISBN:");
        book.setIsbn(scanner.nextLong());
        scanner.nextLine();
    }


    private void category(Book book) {
        System.out.println("Choose category: \n" +
                "1. Romance \n" +
                "2. Horror \n" +
                "3. Science-fiction \n" +
                "4. History \n" +
                "5. Other");

        String category = scanner.nextLine();

        switch (category) {
            case "1":
                book.setCategory(Category.ROMANCE);
                break;
            case "2":
                book.setCategory(Category.HORROR);
                break;
            case "3":
                book.setCategory(Category.SCIENCE_FICTION);
                break;
            case "4":
                book.setCategory(Category.HISTORY);
                break;
            default:
                book.setCategory(Category.OTHER);
                break;
        }
    }

    private void pages(Book book) {
        System.out.println("Enter number of pages:");
        book.setNumberOfPages(scanner.nextInt());
        scanner.nextLine();
    }

    private void description(Book book) {
        System.out.println("Enter description:");
        book.setDescription(scanner.nextLine());
    }

    private void price(Book book) {
        System.out.println("Enter price:");
        Float price = scanner.nextFloat();
        book.setPrice(price);
        scanner.nextLine();
    }

    private void modifyBook() throws ParseException {
        Book tmp = findById();

        String menu = "What do you want to modify? \n" +
                "1. Title \n" +
                "2. Release date \n" +
                "3. ISBN \n" +
                "4. Category \n" +
                "5. Number of pages \n" +
                "6. Description \n" +
                "7. Price \n" +
                "8. That's all, save and exit to main menu. \n" +
                "9. I changed my mind. Don't save any changes and exit to main menu.";

        boolean isFinished = true;

        while (isFinished) {
            System.out.println(menu);
            String answer = scanner.nextLine();
            switch (answer) {
                case "1":
                    title(tmp);
                    break;
                case "2":
                    date(tmp);
                    break;
                case "3":
                    isbn(tmp);
                    break;
                case "4":
                    category(tmp);
                    break;
                case "5":
                    pages(tmp);
                    break;
                case "6":
                    description(tmp);
                    break;
                case "7":
                    price(tmp);
                    break;
                case "8":
                    repository.update(tmp);
                    isFinished = false;
                    break;
                case "9":
                    System.out.println("Exiting.");
                    isFinished = false;
                    break;
                default:
                    System.out.println("No such option available. Nothing has been changed.");
                    break;
            }
        }
    }

    private Book findById() {
        System.out.println("Enter ID of a book you want to find:");
        Integer id = scanner.nextInt();
        Book found = repository.read(id);
        scanner.nextLine();
        System.out.println("Here is a book of ID " + id);
        System.out.println(found.toString());

        return found;
    }

    private void deleteBook() {
        System.out.println("Enter ID of a book you want to remove:");
        repository.delete(scanner.nextInt());
        scanner.nextLine();
    }

    private void exit() {
        repository.cleanUp();
    }

    @Override
    public void run() {
        boolean isFinished = true;
        while (isFinished) {
            switch (printMenu()) {
                case "1":
                    Book book = new Book();
                    try {
                        newBook(book);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        modifyBook();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    findById();
                    break;
                case "4":
                    deleteBook();
                    break;
                case "5":
                    exit();
                    isFinished = false;
                    break;
            }
        }
    }
}
