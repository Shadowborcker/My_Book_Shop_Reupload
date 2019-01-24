import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

//Опция меню для удаления пользователя из базы данных.
class MenuItemUserRemover extends QueryHelper implements MenuItem {

    public String description() {
        return "Removing user from database.";
    }

    public void select() throws IOException {
        String login;
        boolean isPaid = false;
        while (true) {
            login = userInputReader.askString("Enter user's login to remove.");
            List<Order> orderList;
            try {
                orderList = storage.readOrdersFromTable(login, connection);
                for (Order order : orderList
                ) {
                    if (order.getIsPaid()) {
                        isPaid = true;
                    }
                }
                if (!isPaid) {
                    if (!orderList.isEmpty()) {
                        try {
                            System.out.println("Removing user's orders from database.");
                            storage.removeUsersOrdersFromTables(login, connection);
                            System.out.println("User's orders removed");
                        } catch (SQLException e) {
                            System.out.println("Unable to remove user's orders.");
                        }
                    }

                    try {
                        System.out.println(description());
                        storage.removeUserFromTable(login, connection);
                        System.out.println("User removed");
                    } catch (SQLException e) {
                        System.out.println("No such user in database, Sir.");
                    }
                } else System.out.println("Specified user has paid orders and cannot be removed from database.");
            } catch (SQLException e) {
                System.out.println("No orders found for " + login);
                return;
            }
        }


    }
}
