import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class MenuItemCheckOut extends QueryHelper implements MenuItem {


    public String description() {
        return "Paying for your order, Sir";
    }

    public void select() {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        String login = Menu.currentUser.getLogin();
        try {
            storage.readUserFromTable(login, connection);
        } catch (SQLException e) {
            System.out.println("No matching login found in database.");
        }
        List<Order> orders;
        try {
            System.out.println("Showing your order's list.");
            orders = storage.readOrdersFromTable(login, connection);
            boolean allPaid = true;
            int i = 1;
            for (Order order : orders) {
                if (!order.isPaid) {
                    System.out.println(i + ". " + order);
                    allPaid = false;
                    i++;

                }
            }
            if (allPaid) {
                System.out.println("You have no unpaid orders, Sir");
            }
        } catch (SQLException e) {
            System.out.println("You have no active orders.");
            return;
        }

        int i = userInputReader.askInt("Choose order to pay for.");
        Order order = orders.get(i-1);
        if (order.getSum() > Menu.currentUser.getMoney()) {
            System.out.println("YOu don't have enough money to pay for this order, Sir.");
            return;
        }
        System.out.println(description());
        try {
            storage.payForOrder(order, connection);
            order.setPaid(true);
            Menu.currentUser.setMoney(Menu.currentUser.getMoney() - order.getSum());
            System.out.println("Your order has been paid for. Your current balance is " +
                    numberFormat.format(Menu.currentUser.getMoney()) + " Rub.");
        } catch (SQLException e) {
            System.out.println("Unable to process your payment.");
            e.printStackTrace();
        }

    }
}
