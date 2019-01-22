import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Опция меню для добавления книги в магазин или домашнюю библиотеку.
class MenuItemAddBook extends QueryHelper implements MenuItem {

    public String description() {
        return "Adding to books to specified location";
    }

    public void select() throws IOException, SQLException {
        List<Book> books = new ArrayList<>();
        Book book = bookFactory.newBook();
        String location = Menu.submenuLocation();
        book.setQuantity(userInputReader.askInt("How many would you like to add?"));
        books.add(book);
        storage.addBookToTable(books, location, connection);
        System.out.println("Book(s) added.");
        System.out.println();
    }
}
