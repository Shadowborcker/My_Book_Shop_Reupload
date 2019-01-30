import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//Опция меню для сортировки книг в магазине или домашней библиотеке.
class MenuItemBooksSorter extends QueryHelper implements MenuItem {

    public String description() {
        return "Showing sorted books in specified location";
    }

    public void select() throws IOException, SQLException {
        String criteria = Menu.submenuCriteria();
        String location = Menu.submenuLocation();

        List<Book> books = storage.sortBooksInTable(criteria, location, connection);
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
