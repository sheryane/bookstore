package pl.sheryane.bookstore;

import pl.sheryane.bookstore.menu.Menu;

import java.text.ParseException;

public class BookMain {
    public static void main(String[] args) throws ParseException {

        Menu menu = new Menu();
        menu.run();

    }
}