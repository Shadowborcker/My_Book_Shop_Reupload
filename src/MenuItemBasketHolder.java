import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//Опция меню для переноса книги в корзину.
class MenuItemBasketHolder extends QueryHelper implements MenuItem {

    public String description() {
        return "Moving book(s) to your basket.";
    }

    public void select() throws IOException, SQLException {
        int quantity = 0;
        String location;
        List<Book> booksToMove;
        String name, surname, author, title;
        name = userInputReader.askString("Enter author's name");
        surname = userInputReader.askString("Enter author's surname");
        title = userInputReader.askString("Enter title");
        author = surname + " " + name;

        location = "\"SHOP_DEPO\"";

        try {
            booksToMove = storage.searchTableForBook(author, title, location, connection);

            for (Book b : booksToMove) {
                String choice = "y";
                System.out.println(b.toString());
                while (choice.toLowerCase().equals("y") | choice.toLowerCase().equals("n")) {
                    choice = userInputReader.askString("Is this the book you wanted? Y/N");
                    if (choice.toLowerCase().equals("y")) {
                        while (quantity == 0) {
                            quantity = userInputReader.askInt("How many would you like, Sir?");
                            if (quantity <= b.getQuantity()) break;
                            else
                                System.out.println("There is not enough books in store," +
                                        " Sir, set another quantity.");
                        }
                        b.setQuantity(quantity);
                        Menu.currentUserBasket.add(b);
                        System.out.println(b.getQuantity() + " book(s) added to your basket.");
                        break;
                    }
                }

            }

        } catch (NullPointerException e) {
            System.out.println("No such book, sir.");
        }

    }
}
