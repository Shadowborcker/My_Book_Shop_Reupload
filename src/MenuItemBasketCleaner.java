import java.util.List;

//Опция меню для удаления книг из корзины.
class MenuItemBasketCleaner extends MenuHelper implements MenuItem {

    public String description() {
        return "Removing book(s) from your basket.";
    }

    public void select() {
        if (currentUserBasket.isEmpty()) {
            System.out.println("Your basket is empty, Sir");
        } else {
            int i = 1;
            for (Book book : currentUserBasket) {
                System.out.println(i + book.toString());
                i++;
            }
            try {
                currentUserBasket.remove(userInputReader.askInt("Select book to remove"));
                System.out.println(description());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong index selected");
            }
        }


    }
}
