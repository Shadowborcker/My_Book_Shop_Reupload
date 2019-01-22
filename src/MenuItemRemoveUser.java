import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

//Опция меню для удаления пользователя из базы данных.
class MenuItemRemoveUser extends QueryHelper implements MenuItem {

    public String description() {
        return "Removing user's order from database.";
    }

    public void select() throws IOException {
        String login = userInputReader.askString("Enter user's login to remove his order.");
        try {
            storage.removeOrderFromTables(login, connection);
            System.out.println(description());
        } catch (SQLException e) {
            System.out.println("No such user in database, Sir.");
        }
    }
}
