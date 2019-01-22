import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//Опция меню для поиска книги в магазине или домашней библиотеке.
class MenuItemSearch extends QueryHelper implements MenuItem {

    public String description() {
        return "Here is what we managed to find.";
    }

    public void select() throws IOException, SQLException {
        String name, surname, author, title;
        String location = Menu.submenuLocation();
        name = userInputReader.askString("Enter author's name");
        surname = userInputReader.askString("Enter author's surname");
        title = userInputReader.askString("Enter title");
        author = surname + " " + name;

        List<Book> found = storage.searchTableForBook(author, title, location, connection);
        System.out.println(description());

        try {
            for (Book book : found) {
                System.out.println(book);
            }
        } catch (NullPointerException e) {
            System.out.println("We did not find any books matching your criteria.");
        }


        System.out.println();
        connection.close();

    }

}
