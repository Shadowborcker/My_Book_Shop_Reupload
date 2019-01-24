import java.io.IOException;
import java.sql.SQLException;

//Опция меню для удаления заказов пользователя.
public class MenuItemOrderRemover extends QueryHelper implements MenuItem {

    public String description() {
        return "Removing order from database.";
    }

    public void select() throws IOException {
        boolean isPaid;
        MenuItemOrdersDisplay menuItemShowOrders = new MenuItemOrdersDisplay();
        menuItemShowOrders.select();
        int i;
        while (true) {
            i = userInputReader.askInt("Select order to remove");
            Order orderToRemove = menuItemShowOrders.orders.get(i);
            isPaid = orderToRemove.getIsPaid();
            if (isPaid) {
                System.out.println("The order you selected is paid for and cannot be removed.");
            }
            else {
                try {
                    storage.removeOrderFromTables(orderToRemove, connection);
                    break;
                } catch (SQLException e) {
                    System.out.println("Unable to remove order");
                    e.printStackTrace();
                }
            }
        }
    }
}