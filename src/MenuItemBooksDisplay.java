import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//Опция меню для отображения содержимого магазина или домашней библиотеки.
class MenuItemBooksDisplay extends MenuHelper implements MenuItem {

    public String description() {
        return "Showing all books in a specified location";
    }

    public void select() {

        String location = Menu.submenuLocation();
        List<Book> books;


        if (location.equals("\"SHOP_DEPO\"") || location.equals("\"HOME_LIBRARY\"")) {
            try {
                books = storage.readBooksFromTable(location);
                System.out.println(description());
                for (Book b : books) {
                    System.out.println(b.toString());
                }
            } catch (SQLException e) {
                switch (location) {
                    case "\"HOME_LIBRARY\"":
                        System.out.println("Your library is empty, Sir");
                        break;
                    case "\"SHOP_DEPO\"":
                        System.out.println("Store depository is empty, Sir");
                        break;
                }
            }

        } else {
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            double totalprice = 0;
            if (currentUserBasket.isEmpty()) {
                System.out.println("Your basket is empty, Sir");
                return;
            } else {
                int i = 1;
                books = currentUserBasket;
                System.out.println(description());
                for (Book book : books) {
                    System.out.println(i + ". " + book.toString());
                    totalprice += book.getPrice() * book.getQuantity();
                    i++;
                }
                System.out.println("For a total price of " + numberFormat.format(totalprice) + "Rub.");
            }
        }


        System.out.println();
    }

}
