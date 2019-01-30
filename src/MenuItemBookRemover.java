import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Опция меню для удаления книг из магазина или домашней библиотеки.
class MenuItemBookRemover extends QueryHelper implements MenuItem {

    public String description() {
        return "Removing books from a location specified.";
    }

    public void select() throws IOException, SQLException {
        String name, surname, author, title;
        name = userInputReader.askString("Enter author's name");
        surname = userInputReader.askString("Enter author's surname");
        title = userInputReader.askString("Enter title");
        author = surname + " " + name;
        String location = Menu.submenuLocation();
        List<Book> found = new ArrayList<>();
        int index = 1;

        try {
            found = storage.searchTableForBook(author, title, location, connection);
            System.out.println("Books found matching your criteria for deletion.");
            for (Book book : found) {
                index++;
                System.out.println(index + "." + book);
            }
        } catch (NullPointerException e) {
            System.out.println("We did not find any books matching your criteria.");
        }
        int choice = userInputReader.askInt("Select a book from the list to remove");
        Book book = found.get(choice);
        int quantity = userInputReader.askInt("How many would you like to delete?");
        book.setQuantity(quantity);
        storage.removeBookFromTable(book, location, connection);
        System.out.println(description());
        System.out.println();

    }

}
