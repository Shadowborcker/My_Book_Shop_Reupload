import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class Menu {

    private UserInputReader userInputReader = Main.getUserInputReader();
    private Storage storage = Main.getStorage();

    void greetingsMenu() {

        String choice = userInputReader.askString("Please sign up or login to existing account\n" +
                "Sign up\\Log in");
        if (choice.toLowerCase().equals("sign up")) {
            while (true) {
                String login = userInputReader.askString("Please enter desired login");
                String password = userInputReader.askString("Please enter new password");
                double money = userInputReader.askDouble("Please enter your balance");
                Main.setCurrentUser(new User(login));
                User currentUser = Main.getCurrentUser();
                currentUser.setPassword(password);
                currentUser.setMoney(money);
                try {
                    if (storage.userExists(login)) {
                        throw new SQLException();
                    }
                    storage.addUserToTable(currentUser);
                    Main.setCurrentUser(storage.readUserFromTable(login));
                    break;
                } catch (SQLException e) {
                    System.out.println("This login is already preoccupied.");
                }
            }
        } else {
            while (true) {
                User tempUser;
                String login = userInputReader.askString("Please enter login");
                String password = userInputReader.askString("Enter password for " + login);
                try {
                    tempUser = storage.readUserFromTable(login);
                    if (tempUser.getPassword().equals(password)) {
                        Main.setCurrentUser(tempUser);
                        break;
                    } else System.out.println("Invalid login\\password combination try again");
                } catch (SQLException e) {
                    System.out.println("No user found with matching login.");
                }
            }
        }

        System.out.println("Hello, " + Main.getCurrentUser().getLogin() + " , your balance is "
                + Main.getCurrentUser().getMoney() + "Rub." + ", how may I be of service?\n" +
                "Use \"Show menu\" for commands list.");
    }

    private HashMap<String, MenuItem> menuMap() {
        HashMap<String, MenuItem> menumap = new HashMap<>();
        menumap.put("show menu", new MenuItemMenuDisplay()); //Tested
        menumap.put("remove user", new MenuItemUserRemover());
        menumap.put("show books", new MenuItemBooksDisplay()); //Tested
        menumap.put("add book", new MenuItemBookAdder()); //Tested
        menumap.put("remove book", new MenuItemBookRemover()); //Tested
        menumap.put("sort", new MenuItemBooksSorter()); // Tested
        menumap.put("search", new MenuItemBookFinder()); // Tested
        menumap.put("buy", new MenuItemBasketHandler()); //Tested
        menumap.put("clear basket", new MenuItemBasketCleaner()); // Tested
        menumap.put("order", new MenuItemOrderAdder()); // Tested
        menumap.put("show orders", new MenuItemOrdersDisplay()); // Tested
        menumap.put("remove order", new MenuItemOrderRemover());
        menumap.put("pay", new MenuItemPayer()); //Tested
        menumap.put("ship", new MenuItemShipper()); //Tested
        menumap.put("exit", new MenuItemExit()); //Tested

        return menumap;
    }

    private HashMap<String, MenuItem> map = menuMap();

    void chooseMenuItem(String input) throws IOException, SQLException {
        if (map.containsKey(input.toLowerCase())) {
            map.get(input.toLowerCase()).select();
        } else System.out.println("Invalid command, Sir");
    }


    static String submenuLocation() {
        String location = null;
        UserInputReader userInputReader = new UserInputReader();
        while (location == null) {

            String choice = userInputReader.askString("Where would you like to look?\n" +
                    "Store\\Library\\Basket");
            switch (choice.toLowerCase()) {
                case "store": {
                    location = "\"SHOP_DEPO\"";
                    break;
                }
                case "library": {
                    location = "\"HOME_LIBRARY\"";
                    break;
                }
                case "basket": {
                    location = "\"USER_BASKET\"";
                    break;
                }
                default:
                    System.out.println("Invalid location, please try again.");
            }
        }
        return location;
    }

    static String submenuCriteria() {
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
