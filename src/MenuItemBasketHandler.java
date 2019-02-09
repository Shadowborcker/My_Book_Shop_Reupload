import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//Опция меню для переноса книги в корзину.
class MenuItemBasketHandler extends MenuHelper implements MenuItem {

    public String description() {
        return "Moving book(s) to your basket.";
    }

    public void select() {
        List<Book> currentUserBasket = Main.getCurrentUserBasket();
        int quantity;
        String location;
        Book bookToMove;
        List<Book> booksToMove;
        String name, surname, author, title;
        name = userInputReader.askString("Enter author's name");
        surname = userInputReader.askString("Enter author's surname");
        title = userInputReader.askString("Enter title");
        author = surname + " " + name;

        location = "\"SHOP_DEPO\"";

        try {
            booksToMove = storage.searchTableForBook(author, title, location);
            int i = 1;
            int choice;
            for (Book b : booksToMove) {
                System.out.println(i + ". " + b.toString());
                i++;
            }

            while (true) {
                choice = userInputReader.askInt("Choose the book you wanted");
                if (choice != 0 | choice < (booksToMove.size() - 1)) {
                    bookToMove = booksToMove.get(choice - 1);
                    break;
                } else {
                    System.out.println("Wrong index.");
                }
            }
            while (true) {
                quantity = userInputReader.askInt("How many would you like, Sir?");
                if (quantity <= bookToMove.getQuantity()) {
                    bookToMove.setQuantity(quantity);
                    break;
                } else
                    System.out.println("There is not enough books in store," +
                            " Sir, set another quantity.");
            }
            bookToMove.setQuantity(quantity);
            currentUserBasket.add(bookToMove);
            System.out.println(bookToMove.getQuantity() + " book(s) added to your basket.");

        } catch (SQLException e) {
            System.out.println("We could not add the book to your basket.");
        } catch (IOException e) {
            System.out.println("No books found matching your criteria.");
        }

    }
}
