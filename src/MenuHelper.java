import java.sql.Connection;
import java.util.List;

public class MenuHelper {
    User currentUser = Main.getCurrentUser();
    BookFactory bookFactory = Main.getBookFactory();
    UserInputReader userInputReader = Main.getUserInputReader();
    Storage storage = Main.getStorage();
    List<Book> currentUserBasket = Main.getCurrentUserBasket();
}
