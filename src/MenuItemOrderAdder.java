import java.sql.SQLException;

//Опция меню для размещения заказов в базе данных.
class MenuItemOrderAdder extends MenuHelper implements MenuItem {

    public String description() {
        return "Ordering your books, Sir";
    }

    public void select() {
        if (currentUserBasket.isEmpty()) {
            System.out.println("Your basket is empty, Sir");
            return;
        }
        Order order = new Order(Main.getCurrentUser(), currentUserBasket);
        try {
            System.out.println(description());
            storage.addOrderToTable(order);
            System.out.println("Your order is complete and awaits payment");
        } catch (SQLException e) {
            System.out.println("Order can not be processed due to book shortage.");
        }
    }
}
