import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu {

    private static List<Book> currentUserBasket = new ArrayList<>();
    private User currentUser;


    void greetingsMenu() throws IOException {
        UserInputReader userInputReader = new UserInputReader();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();
        Storage storage = new Storage();
        String choice = userInputReader.askString("Please sign up or login to existing account\n" +
                "Sign up\\Log in");
        if (choice.toLowerCase().equals("sign up")) {
            while (true) {
                String login = userInputReader.askString("Please enter desired login");
                String password = userInputReader.askString("Please enter new password");
                double money = userInputReader.askDouble("Please enter your balance");
                currentUser = new User(login, password, money);
                try {
                    storage.addUserToTable(currentUser, connection);
                    break;
                } catch (SQLException e) {
                    System.out.println("This login is already preoccupied.");
                }
            }
        } else {
            while (true) {
                String login = userInputReader.askString("Please enter login");
                try {
                    currentUser = storage.readUserFromTable(login, connection);
                    break;
                } catch (SQLException e) {
                    System.out.println("User not found");
                }
            }
        }

        System.out.println("Hello, " + currentUser.getLogin() + " , your balance is "
                + currentUser.getMoney() + "Rub." + ", how may I be of service?\n" +
                "Use \"Show menu\" for commands list.");
    }

    private HashMap<String, MenuItem> menuMap() {
        HashMap<String, MenuItem> menumap = new HashMap<>();
        menumap.put("show menu", new MenuItemShowMenu());
//        menumap.put("add user", new MenuItemAddUser());
        menumap.put("remove user", new MenuItemRemoveUser());
//        menumap.put("login", new MenuItemLogin());
        menumap.put("show books", new MenuItemShowBooks());
        menumap.put("add book", new MenuItemAddBook());
        menumap.put("remove book", new MenuItemRemoveBook());
        menumap.put("sort", new MenuItemSort());
        menumap.put("search", new MenuItemSearch());
        menumap.put("move to basket", new MenuItemMoveToBasket());
        menumap.put("remove from basket", new MenuItemRemoveFromBasket());
        menumap.put("show basket", new MenuItemShowBasket());
        menumap.put("create order", new MenuItemCreateOrder());
        menumap.put("show orders", new MenuItemShowOrders());
//        menumap.put("remove order", new MenuItemRemoveOrder());
//        menumap.put("checkout", new MenuItemCheckOut());
        menumap.put("exit", new MenuItemExit());

        return menumap;
    }

    private HashMap<String, MenuItem> map = menuMap();

    void chooseMenuItem(String input) throws IOException, SQLException {
        if (map.containsKey(input.toLowerCase())) {
            map.get(input.toLowerCase()).select();
        } else System.out.println("Invalid command, Sir");
    }


    interface MenuItem {
        String description();

        void select() throws IOException, SQLException;
    }

    private String submenuLocation() throws IOException {
        String location = null;
        UserInputReader userInputReader = new UserInputReader();
        while (location == null) {

            String choice = userInputReader.askString("Where would you like to look?\n" +
                    "Store\\Library");
            switch (choice.toLowerCase()) {
                case "store": {
                    location = "\"SHOP_DEPO\"";
                    break;
                }
                case "library": {
                    location = "\"HOME_LIBRARY\"";
                    break;
                }
                default:
                    System.out.println("Invalid location");
            }
        }
        return location;
    }

    private String submenuCriteria() throws IOException {
        String criteria = null;
        UserInputReader userInputReader = new UserInputReader();
        while (criteria == null) {

            String choice = userInputReader.askString("Enter sorting criteria.\n" +
                    "Author\\Title\\Publisher\\Year\\Pages\\Price\\Quantity");
            switch (choice.toLowerCase()) {
                case "author": {
                    criteria = "author";
                    break;
                }
                case "title": {
                    criteria = "title";
                    break;
                }
                case "publisher": {
                    criteria = "publisher";
                    break;
                }
                case "year": {
                    criteria = "year";
                    break;
                }
                case "pages": {
                    criteria = "pages";
                    break;
                }
                case "price": {
                    criteria = "price";
                    break;
                }
                case "quantity": {
                    criteria = "quantity";
                    break;
                }
                default:
                    System.out.println("Invalid criteria");
            }
        }
        return criteria;

    }


    class MenuItemShowMenu implements MenuItem {
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

    class MenuItemExit implements MenuItem {
        public String description() {
            return "Terminating program.\n" +
                    "See you again, sir!";
        }

        public void select() {
            System.out.println(description());
            System.exit(0);
        }

    }


    class MenuItemShowBooks implements MenuItem {
        Storage storage = new Storage();
        String location = null;
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();


        public String description() {
            return "Showing all books in specified location";
        }

        public void select() throws IOException, SQLException {

            location = submenuLocation();
            List<Book> books;


            books = storage.readBooksFromTable(location, connection);


            if (books.size() != 0) {
                System.out.println(description());
                for (Book b : books) {
                    System.out.println(b.toString());

                }

            } else {
                switch (location) {
/*                    case "\"USER_BASKET\"":
                        System.out.println("Your basket is empty, Sir");
                        break;*/
                    case "\"HOME_LIBRARY\"":
                        System.out.println("Your library is empty, Sir");
                        break;
                    case "\"SHOP_DEPO\"":
                        System.out.println("Store depository is empty, Sir");
                }
            }
            System.out.println();
            connection.close();

        }

    }

    class MenuItemAddBook implements MenuItem {
        UserInputReader userInputReader = new UserInputReader();
        Storage storage = new Storage();
        BookFactory bookFactory = new BookFactory();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();
        String location;
        List<Book> books = new ArrayList<>();


        public String description() {
            return "Adding to books to specified location";
        }

        public void select() throws IOException, SQLException {
            Book book = bookFactory.newBook();
            location = submenuLocation();
            book.setQuantity(userInputReader.askInt("How many would you like to add?"));
            books.add(book);
            storage.addBookToTable(books, location, connection);
            System.out.println("Book(s) added.");
            System.out.println();
        }
    }

    class MenuItemRemoveBook implements MenuItem {
        Storage storage = new Storage();
        String location = null;
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();
        UserInputReader userInputReader = new UserInputReader();
        String name, surname, author, title;


        public String description() {
            return "Removing books from a location specified.";
        }

        public void select() throws IOException, SQLException {
            name = userInputReader.askString("Enter author's name");
            surname = userInputReader.askString("Enter author's surname");
            title = userInputReader.askString("Enter title");
            author = surname + " " + name;
            location = submenuLocation();
            List<Book> found = new ArrayList<>();
            int index = 0;

            try {
                found = storage.searchTableForBook(author, title, location, connection);
                System.out.println("Books found matching your criteria for deletion.");
                for (Book book : found) {
                    index++;
                    System.out.println(index + "." + book);
                }
            } catch (NullPointerException e) {
                System.out.println("We did not find any books matching your criteria.");
            }
            int choice = userInputReader.askInt("Select a book from the list to remove");
            Book book = found.get(choice);
            int quantity = userInputReader.askInt("How many would you like to delete?");
            book.setQuantity(quantity);
            storage.removeBookFromTable(book, location, connection);
            System.out.println(description());
            System.out.println();
            connection.close();
        }

    }

    class MenuItemSort implements MenuItem {
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();
        Storage storage = new Storage();
        String criteria;
        String location = null;


        public String description() {
            return "Showing sorted books in specified location";
        }

        public void select() throws IOException, SQLException {
            criteria = submenuCriteria();
            location = submenuLocation();

            List<Book> books = storage.sortBooksInTable(criteria, location, connection);
            if (books.size() != 0) {
                System.out.println(description());
                for (Book b : books) {
                    System.out.println(b.toString());
                }

            } else {
                System.out.println("Nothing to sort.");
            }
            System.out.println();
            connection.close();

        }

    }

    class MenuItemSearch implements MenuItem {
        Storage storage = new Storage();
        String location = null;
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();
        UserInputReader userInputReader = new UserInputReader();
        String name, surname, author, title;


        public String description() {
            return "Here is what we managed to find.";
        }

        public void select() throws IOException, SQLException {
            name = userInputReader.askString("Enter author's name");
            surname = userInputReader.askString("Enter author's surname");
            title = userInputReader.askString("Enter title");
            author = surname + " " + name;
            location = submenuLocation();

            List<Book> found = storage.searchTableForBook(author, title, location, connection);
            System.out.println(description());

            try {
                for (Book book : found) {
                    System.out.println(book);
                }
            } catch (NullPointerException e) {
                System.out.println("We did not find any books matching your criteria.");
            }


            System.out.println();
            connection.close();

        }

    }

    class MenuItemMoveToBasket implements MenuItem {
        Storage storage = new Storage();
        String location = null;
        List<Book> booksToMove;
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();
        UserInputReader userInputReader = new UserInputReader();
        String name, surname, author, title;
        int quantity;


        public String description() {
            return "Moving book(s) to your basket.";
        }

        public void select() throws IOException, SQLException {
            name = userInputReader.askString("Enter author's name");
            surname = userInputReader.askString("Enter author's surname");
            title = userInputReader.askString("Enter title");
            author = surname + " " + name;

            location = "\"SHOP_DEPO\"";

            try {
                booksToMove = storage.searchTableForBook(author, title, location, connection);

                for (Book b : booksToMove) {
                    String choice = "y";
                    System.out.println(b.toString());
                    while (choice.toLowerCase().equals("y") | choice.toLowerCase().equals("n")) {
                        choice = userInputReader.askString("Is this the book you wanted? Y/N");
                        if (choice.toLowerCase().equals("y")) {
                            while (quantity == 0) {
                                quantity = userInputReader.askInt("How many would you like, Sir?");
                                if (quantity <= b.getQuantity()) break;
                                else
                                    System.out.println("There is not enough books in store," +
                                            " Sir, set another quantity.");
                            }
                            b.setQuantity(quantity);
                            currentUserBasket.add(b);
                            System.out.println(b.getQuantity() + " book(s) added to your basket.");
                        }
                    }

                }

            } catch (NullPointerException e) {
                System.out.println("No such book, sir.");
            }

        }
    }

    //Метод размещения заказов в базе данных.
    class MenuItemCreateOrder implements MenuItem {
        Storage storage = new Storage();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();


        public String description() {
            return "Ordering your books, Sir";
        }

        public void select() {
            if (currentUserBasket.isEmpty()) {
                System.out.println("Your basket is empty, Sir");
                return;
            }
            Order order = new Order(currentUser, currentUserBasket);
            try {
                System.out.println(description());
                storage.addOrderToTable(order, connection);
                System.out.println("Your order is complete and awaits payment");
            } catch (SQLException e) {
                System.out.println("Order can not be processed due to book shortage.");
            }
        }
    }

    //Метод удаления пользователя из базы данных.
    class MenuItemRemoveUser implements MenuItem {
        UserInputReader userInputReader = new UserInputReader();
        Storage storage = new Storage();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();

        public String description() {
            return "Removing user from database.";
        }

        public void select() throws IOException, SQLException {
            String login = userInputReader.askString("Enter user's login to remove");
            try {
                storage.removeUserFromTable(login, connection);
                System.out.println(description());
            } catch (SQLException e) {
                System.out.println("No such user in database, Sir.");
            }
        }
    }

    //Метод удаления книг из корзины
    class MenuItemRemoveFromBasket implements MenuItem {
        UserInputReader userInputReader = new UserInputReader();

        public String description() {
            return "Removing book(s) from your basket.";
        }

        public void select() throws IOException {
            if (currentUserBasket.isEmpty()) {
                System.out.println("Your basket is empty, Sir");
                return;
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

    //Метод просмотра содержимого корзины
    class MenuItemShowBasket implements MenuItem {
        public String description() {
            return "Showing book(s) in your basket.";
        }

        public void select() {
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            double totalprice = 0;
            if (currentUserBasket.isEmpty()) {
                System.out.println("Your basket is empty, Sir");
                return;
            } else {
                int i = 1;
                System.out.println(description());
                for (Book book : currentUserBasket) {
                    System.out.println(i + book.toString());
                    totalprice += book.getPrice() * book.getQuantity();
                    i++;
                }
                System.out.println("For a total price of " + numberFormat.format(totalprice) + "Rub.");
            }
        }
    }

    //Метод просмотра заказов пользователя
    class MenuItemShowOrders implements MenuItem {
        List<Order> orders = new ArrayList<>();
        Storage storage = new Storage();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();

        public String description() {
            return "Showing current users's orders.";
        }

        public void select() {
            DecimalFormat numberFormat = new DecimalFormat("#.00");
            try {
                System.out.println(description());
                orders = storage.readOrdersFromTable(currentUser, connection);
                for (Order order : orders) {
                    order.toString();
                }
            } catch (SQLException e) {
                System.out.println("Current user has no active orders.");
            }
        }
    }

/*    class MenuItemAddUser implements MenuItem {
        UserInputReader userInputReader = new UserInputReader();
        Storage storage = new Storage();
        DBConnection dbConnection = new DBConnection();
        Connection connection = dbConnection.dbConnect();

        public String description() {
            return "Adding user to database";
        }

        public void select() throws IOException, SQLException {
            String login = userInputReader.askString("Enter Login");
            String password = userInputReader.askString("Enter Password");
            double money = userInputReader.askDouble("Enter user's account balance");
            currentUser = new User(login, password, money);
            storage.addUserToTable(currentUser, connection);
            System.out.println("New user added");
            System.out.println();
        }
    }*/


}
