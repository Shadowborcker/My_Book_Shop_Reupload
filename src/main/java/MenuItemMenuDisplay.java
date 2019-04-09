//Опция меню для отображения всех пунктов меню.
class MenuItemMenuDisplay implements MenuItem {
    public String description() {
        return "Showing all available commands";
    }

    public void select() {

        System.out.println(description());
        System.out.println("Show menu - displays this menu\n" +
                "Show books - displays list of books in a specified location\n" +
                "Add book - adds new book to the store\n" +
                "Remove book - removes selected book from the store\n" +
                "Sort - displays books in a specified location sorted by user defined criteria\n" +
                "Search - searches for a certain book in a specified location\n" +
                "Buy - puts specified book in your basket\n" +
                "Clear basket - removes all the books from your basket\n" +
                "Order - forms an order with all the books from your basket\n" +
                "Remove order - removes previously formed order if it has not been paid for\n" +
                "Show orders - displays all user's orders\n" +
                "Pay - pays for selected order with user's funds\n" +
                "Ship - ships books from paid order to user's library\n" +
                "Remove user - removes specified user from database in he has no paid orders\n" +
                "Exit - closes Black Books");
    }

}
