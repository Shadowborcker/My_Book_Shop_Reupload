//Опция меню для отображения всех пунктов меню.
class MenuItemMenuDisplay extends QueryHelper implements MenuItem {
    public String description() {
        return "Showing all available commands";
    }

    public void select() {

        System.out.println(description());
        System.out.println("Show books - displays the list of all books in specified location\n" +
                "Refill  - refills specified book storage\n" +
                "Add books - adds books to specified location\n" +
                "Remove books - removes selected books from specified location\n" +
                "Sort - sorts all books by user defined criteria\n" +
                "Search - searches for a certain book\n" +
                "Cash out - pays for all the books in your basket and\n" +
                " sends them to your home library\n" +
                "Exit - closes Black Books");
    }

}
