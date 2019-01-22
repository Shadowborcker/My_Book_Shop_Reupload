import java.text.DecimalFormat;

//Опция меню для просмотра содержимого корзины.
class MenuItemShowBasket extends QueryHelper implements MenuItem {

    public String description() {
        return "Showing book(s) in your basket.";
    }

    public void select() {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        double totalprice = 0;
        if (Menu.currentUserBasket.isEmpty()) {
            System.out.println("Your basket is empty, Sir");
            return;
        } else {
            int i = 1;
            System.out.println(description());
            for (Book book : Menu.currentUserBasket) {
                System.out.println(i + book.toString());
                totalprice += book.getPrice() * book.getQuantity();
                i++;
            }
            System.out.println("For a total price of " + numberFormat.format(totalprice) + "Rub.");
        }
    }
}
