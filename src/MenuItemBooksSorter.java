import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

//Опция меню для сортировки книг в магазине или домашней библиотеке.
class MenuItemBooksSorter extends MenuHelper implements MenuItem {

    private List<Book> sortBooks(String criteria, List<Book> books) {
        switch (criteria) {
            case "author": {
                books.sort(Comparator.comparing(Book::getAuthor));
                break;
            }
            case "title": {
                books.sort(Comparator.comparing(Book::getTitle));
                break;
            }
            case "publisher": {
                books.sort(Comparator.comparing(Book::getPublisher));
                break;
            }
            case "year": {
                books.sort(Comparator.comparing(Book::getYear));
                break;
            }
            case "pages": {
                books.sort(Comparator.comparing(Book::getPages));
                break;
            }
            case "price": {
                books.sort(Comparator.comparing(Book::getPrice));
                break;
            }
            case "quantity": {
                books.sort(Comparator.comparing(Book::getQuantity));
                break;
            }
        }
        return books;
    }

    public String description() {
        return "Showing sorted books in specified location";
    }

    public void select() throws SQLException {
        List<Book> currentUserBasket = Main.getCurrentUserBasket();
        String criteria = Menu.submenuCriteria();
        String location = Menu.submenuLocation();
        List<Book> books = new ArrayList<>();


        switch (location) {
            case ("\"SHOP_DEPO\""): {
                books = storage.sortBooksInTable(criteria, location);
                break;
            }

            case ("\"HOME_LIBRARY\""): {
                books = sortBooks(criteria, storage.readBooksFromTable(location));
                break;

            }
            case ("\"USER_BASKET\""): {
                books = sortBooks(criteria, currentUserBasket);
                break;
            }
        }


        if (books.size() != 0) {
            System.out.println(description());
            for (Book b : books) {
                System.out.println(b.toString());
            }

        } else {
            System.out.println("Nothing to sort.");
        }
        System.out.println();


    }

}
