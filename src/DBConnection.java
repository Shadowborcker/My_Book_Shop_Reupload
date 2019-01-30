
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

class DBConnection {

    static UserInputReader userInputReader = new UserInputReader();
    static String user = userInputReader.askString("Enter user name");
    static String password = userInputReader.askString("Enter password");


    Connection serverConnect() {
        Connection connection = null;
        try {

            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            user, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to server");
        return connection;
    }

    Connection dbConnect() {
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/BOOK_SHOP_PROJECT",
                            user, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to database");
        return connection;
    }
}

