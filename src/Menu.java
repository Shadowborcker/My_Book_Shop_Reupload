import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu {

    static List<Book> currentUserBasket = new ArrayList<>();
    static User currentUser;


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
                currentUser = new User(login);
                currentUser.setPassword(password);
                currentUser.setMoney(money);
                try {
                    storage.addUserToTable(currentUser, connection);
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
                    tempUser = storage.readUserFromTable(login, connection);
                    if (tempUser.getPassword().equals(password)) {
                        currentUser = tempUser;
                        break;
                    }
                    else System.out.println("Invalid login\\password combination try again");
                } catch (SQLException e) {
                    System.out.println("No user found with matching login.");
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
        menumap.put("remove order", new MenuItemRemoveOrders());
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


    static String submenuLocation() throws IOException {
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

    static String submenuCriteria() throws IOException {
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
