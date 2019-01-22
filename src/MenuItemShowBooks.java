import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//Опция меню для отображения содержимого магазина или домашней библиотеки.
class MenuItemShowBooks extends QueryHelper implements MenuItem {

    public String description() {
        return "Showing all books in specified location";
    }

    public void select() throws IOException, SQLException {

        String location = Menu.submenuLocation();
        List<Book> books;


        books = storage.readBooksFromTable(location, connection);


        if (books.size() != 0) {
            System.out.println(description());
            for (Book b : books) {
                System.out.println(b.toString());

            }

        } else {
            switch (location) {
/*                    case "\"USER_BASKET\"":
                    System.out.println("Your basket is empty, Sir");
                    break;*/
                case "\"HOME_LIBRARY\"":
                    System.out.println("Your library is empty, Sir");
                    break;
                case "\"SHOP_DEPO\"":
                    System.out.println("Store depository is empty, Sir");
            }
        }
        System.out.println();
        connection.close();

    }

}
