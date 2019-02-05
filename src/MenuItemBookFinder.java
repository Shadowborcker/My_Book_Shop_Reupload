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
            found = storage.searchTableForBook(author, title, location);
        } else {
            for (Book b : currentUserBasket) {
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
        }

        System.out.println();


    }

}
