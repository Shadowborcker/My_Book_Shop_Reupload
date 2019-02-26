import java.util.List;

//Опция меню для удаления книг из корзины.
class MenuItemBasketCleaner extends MenuHelper implements MenuItem {

    public String description() {
        return "Removing book(s) from your basket.";
    }

    public void select() {
        List<Book> currentUserBasket = Main.getCurrentUserBasket();
        if (currentUserBasket.isEmpty()) {
            System.out.println("Your basket is empty, Sir");
        } else {
            int i = 1;
            for (Book book : currentUserBasket) {
                System.out.println(i + ". " + book.toString());
                i++;
            }
            try {


                while (true) {
                    i = userInputReader.askInt("Select book to remove");
                    if (i != 0 | i < (currentUserBasket.size() - 1)) {
                        currentUserBasket.remove(i - 1);
                        break;
                    }
                }
                System.out.println(description());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong index selected");
            }
        }


    }
}
