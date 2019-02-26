import java.sql.SQLException;

//Опция меню для удаления заказов пользователя.
public class MenuItemOrderRemover extends MenuHelper implements MenuItem {

    public String description() {
        return "Removing order from database.";
    }

    public void select() {
        boolean isPaid;
        MenuItemOrdersDisplay menuItemShowOrders = new MenuItemOrdersDisplay();
        menuItemShowOrders.select();
        int i;

        while (true) {
            i = userInputReader.askInt("Select order to remove");
            if (i != 0 | i < (menuItemShowOrders.orders.size() - 1)) {
                Order orderToRemove = menuItemShowOrders.orders.get(i-1);
                isPaid = orderToRemove.getIsPaid();
                if (isPaid) {
                    System.out.println("The order you selected is paid for and cannot be removed.");
                    return;
                }
                else {
                    try {
                        System.out.println(description());
                        storage.removeOrderFromTables(orderToRemove, true);
                        System.out.println("Order removed successfully.");
                        break;
                    } catch (SQLException e) {
                        System.out.println("Unable to remove order");
                        e.printStackTrace();
                    }
                }

            }
            else System.out.println("No such order in list.");

        }
    }
}
