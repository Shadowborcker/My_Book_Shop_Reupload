import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Опция меню для удаления книг из магазина или домашней библиотеки.
class MenuItemBookRemover extends MenuHelper implements MenuItem {

    public String description() {
        return "Removing books from the store.";
    }

    public void select() throws SQLException {
        String name, surname, author, title;
        name = userInputReader.askString("Enter author's name");
        surname = userInputReader.askString("Enter author's surname");
        title = userInputReader.askString("Enter title");
        author = surname + " " + name;
        String location = "\"SHOP_DEPO\"";
        List<Book> found;
        int index = 1;

        try {
            found = storage.searchTableForBook(author, title, location);
            System.out.println("Books found matching your criteria for deletion.");
            for (Book book : found) {
                System.out.println(index + "." + book);
                index++;
            }
        } catch (IOException e) {
            System.out.println("We did not find any books matching your criteria.");
            return;
        }
        Book book;
        int choice;
        while (true) {
            choice = userInputReader.askInt("Select a book from the list to remove");

            if (choice != 0 | choice < (found.size() - 1)) {
                book = found.get(choice - 1);
                break;
            }
        }

        int quantity = userInputReader.askInt("How many would you like to delete?");
        book.setQuantity(quantity);
        storage.removeBookFromTable(book, location);
        System.out.println(description());
        System.out.println();

    }

}
