import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Опция меню для просмотра заказов пользователя.
class MenuItemShowOrders extends QueryHelper implements MenuItem {


    public String description() {
        return "Showing current users's orders.";
    }

    public void select() throws IOException {
        String login = userInputReader.askString("Enter user's login to look for his orders list.");
        User user = storage.searchTableForUser;
        List<Order> orders;
        try {
            System.out.println(description());
            orders = storage.readOrdersFromTable(user, connection);
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