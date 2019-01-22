import java.io.IOException;
import java.sql.SQLException;

//Опция меню для удаления заказов пользователя.
public class MenuItemRemoveOrder extends QueryHelper implements MenuItem {

    public String description() {
        return "Removing order from database.";
    }

    public void select() throws IOException {
        String login = userInputReader.askString("Enter user's login to remove");
        try {
            storage.removeUserFromTable(login, connection);
            System.out.println(description());
        } catch (SQLException e) {
            System.out.println("No such user in database, Sir.");
        }
    }
}
