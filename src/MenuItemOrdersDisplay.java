import java.sql.SQLException;
import java.util.List;

//Опция меню для просмотра заказов пользователя.
class MenuItemOrdersDisplay extends MenuHelper implements MenuItem {

    List<Order> orders;

    public String description() {
        return "Showing users's orders.";
    }

    public void select() {
        String login = userInputReader.askString("Enter user's login to look for his orders list.");
        try {
            storage.readUserFromTable(login);
        } catch (SQLException e) {
            System.out.println("No matching login found in database.");
        }

        try {
            System.out.println(description());
            orders = storage.readOrdersFromTable(login);
            int i = 1;
            for (Order order : orders) {
                System.out.println(i + ". " + order);
                i++;
            }
        } catch (SQLException e) {
            System.out.println("User has no active orders.");
        }
    }
}
