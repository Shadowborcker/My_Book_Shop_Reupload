import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//Опция меню для отображения содержимого магазина или домашней библиотеки.
class MenuItemBooksDisplay extends QueryHelper implements MenuItem {

    public String description() {
        return "Showing all books in a specified location";
    }

    public void select() throws IOException, SQLException {

        String location = Menu.submenuLocation();
        List<Book> books = new ArrayList<>();


        if (location.equals("\"SHOP_DEPO\"") || location.equals("\"HOME_LIBRARY\"")) {
            books = storage.readBooksFromTable(location, connection);
        } else {
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            double totalprice = 0;
            if (Menu.currentUserBasket.isEmpty()) {
                System.out.println("Your basket is empty, Sir");
                return;
            } else {
                int i = 1;
                System.out.println(description());
                for (Book book : Menu.currentUserBasket) {
                    System.out.println(i + ". " + book.toString());
                    totalprice += book.getPrice() * book.getQuantity();
                    i++;
                }
                System.out.println("For a total price of " + numberFormat.format(totalprice) + "Rub.");
            }
        }
        if (books.size() != 0) {
            System.out.println(description());
            for (Book b : books) {
                System.out.println(b.toString());
            }
        } else {
            switch (location) {
                case "\"USER_BASKET\"":
                    System.out.println("Your basket is empty, Sir");
                    break;
                case "\"HOME_LIBRARY\"":
                    System.out.println("Your library is empty, Sir");
                    break;
                case "\"SHOP_DEPO\"":
                    System.out.println("Store depository is empty, Sir");
            }
        }
        System.out.println();

    }

}
