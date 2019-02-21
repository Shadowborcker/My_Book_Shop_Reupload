import java.sql.SQLException;
import java.util.List;

public class MenuItemShipper extends MenuHelper implements MenuItem {

    private List<Order> orders;

    public String description() {
        return "Shipping items to user's address.";
    }

    public void select() {
        boolean isPaid;
        MenuItemOrdersDisplay menuItemShowOrders = new MenuItemOrdersDisplay();
        menuItemShowOrders.select();
        int i;

        while (true) {
            i = userInputReader.askInt("Select order to ship");
            if (i != 0 | i < (menuItemShowOrders.orders.size() - 1)) {
                Order orderToShip = menuItemShowOrders.orders.get(i - 1);
                isPaid = orderToShip.getIsPaid();
                if (!isPaid) {
                    System.out.println("The order you selected has not been paid for and cannot be shipped.");
                    return;
                } else {
                    try {
                        System.out.println(description());
                        storage.shipOrder(orderToShip);
                        System.out.println("Order shipped successfully.");
                        break;
                    } catch (SQLException e) {
                        System.out.println("Unable to ship order");
                        e.printStackTrace();
                    }
                }

            } else System.out.println("No such order in list.");

        }
    }
}
