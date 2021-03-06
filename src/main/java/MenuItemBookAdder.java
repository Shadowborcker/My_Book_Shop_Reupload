
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Опция меню для добавления книги в магазин или домашнюю библиотеку.
class MenuItemBookAdder extends MenuHelper implements MenuItem {

    public String description() {
        return "Adding to books to the store";
    }

    public void select() throws SQLException {
        List<Book> books = new ArrayList<>();
        Book book = bookFactory.newBook();
        String location = "\"SHOP_DEPO\"";
        book.setQuantity(userInputReader.askInt("How many would you like to add?"));
        books.add(book);
        storage.addBookToTable(books, location);
        System.out.println("Book(s) added.");
        System.out.println();
    }
}
