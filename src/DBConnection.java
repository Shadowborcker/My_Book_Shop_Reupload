
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    static String user;
    static String password;

    static {
        UserInputReader userInputReader = new UserInputReader();
        try {
            user = userInputReader.askString("Enter user name");
            password = userInputReader.askString("Enter password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Connection serverConnect() {
        Connection connection = null;
        try {

            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/",
                            user, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to server");
        return connection;
    }

    public Connection dbConnect() {
        Connection connection = null;
        try {
            connection = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/BOOK_SHOP_PROJECT",
                            user, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to database");
        return connection;
    }
}

