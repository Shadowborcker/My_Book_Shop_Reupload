import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Опция меню для просмотра заказов пользователя.
class MenuItemShowOrders extends QueryHelper implements MenuItem {

    List<Order> orders;

    public String description() {
        return "Showing current users's orders.";
    }

    public void select() throws IOException {
        String login = userInputReader.askString("Enter user's login to look for his orders list.");
        User user = null;
        try {
            user = storage.readUserFromTable(login, connection);
        } catch (SQLException e) {
            System.out.println("No matching login found in database.");
        }

        try {
            System.out.println(description());
            orders = storage.readOrdersFromTable(login, connection);
            int i = 0;
            for (Order order : orders) {
                System.out.println(i + ". " + order);
                i++;
            }
        } catch (SQLException e) {
            System.out.println("Current user has no active orders.");
        }
    }
}
