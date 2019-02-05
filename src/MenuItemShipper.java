import java.sql.SQLException;
import java.util.List;

public class MenuItemShipper extends MenuHelper implements MenuItem {

    private List<Order> orders;
    private int i = 1;

    public String description() {
        return "Shipping items to user's address.";
    }

    public void select() {
        Order orderToShip;
        String login = userInputReader.askString("Enter user's login to look for his orders list.");
        try {
            storage.readUserFromTable(login);
        } catch (SQLException e) {
            System.out.println("No matching login found in database.");
        }

        try {
            orders = storage.readOrdersFromTable(login);

            for (Order order : orders) {
                System.out.println(i + ". " + order);
                i++;
            }
        } catch (SQLException e) {
            System.out.println("Current user has no active orders.");
        }

        orderToShip = orders.get(userInputReader.askInt("Choose order to ship"));
//        storage.shipOrder(orderToShip);


    }
}
