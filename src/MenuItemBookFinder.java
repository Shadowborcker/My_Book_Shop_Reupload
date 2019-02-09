import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Опция меню для поиска книги в магазине или домашней библиотеке.
class MenuItemBookFinder extends MenuHelper implements MenuItem {

    public String description() {
        return "Here is what we managed to find.";
    }

    public void select() throws SQLException {
        String name, surname, author, title;
        String location = Menu.submenuLocation();
        name = userInputReader.askString("Enter author's name");
        surname = userInputReader.askString("Enter author's surname");
        title = userInputReader.askString("Enter title");
        author = surname + " " + name;


        List<Book> found = new ArrayList<>();
        if (location.equals("\"SHOP_DEPO\"") || location.equals("\"HOME_LIBRARY\"")) {
            try {
                found = storage.searchTableForBook(author, title, location);
            } catch (IOException e) {
                System.out.println("No books matching your criteria.");
                return;
            }
        } else {
            for (Book b : Main.getCurrentUserBasket()) {
                if (b.getAuthor().toLowerCase().equals(author.toLowerCase())) {
                    found.add(b);
                }

            }
        }
        System.out.println(description());

        try {
            for (Book book : found) {
                System.out.println(book);
            }
        } catch (NullPointerException e) {
            System.out.println("We did not find any books matching your criteria.");
            return;
        }

        System.out.println();


    }

}
