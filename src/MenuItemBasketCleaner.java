import java.io.IOException;

//Опция меню для удаления книг из корзины.
class MenuItemBasketCleaner extends QueryHelper implements MenuItem {

    public String description() {
        return "Removing book(s) from your basket.";
    }

    public void select() throws IOException {
        if (Menu.currentUserBasket.isEmpty()) {
            System.out.println("Your basket is empty, Sir");
            return;
        } else {
            int i = 1;
            for (Book book : Menu.currentUserBasket) {
                System.out.println(i + book.toString());
                i++;
            }
            try {
                Menu.currentUserBasket.remove(userInputReader.askInt("Select book to remove"));
                System.out.println(description());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Wrong index selected");
            }
        }


    }
}
