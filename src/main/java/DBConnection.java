import java.sql.Connection;
import java.sql.DriverManager;

class DBConnection {

    static {
        System.out.println("Application uses PostgreSQL to store it's data. \n" +
                "We require your PostgreSQL username and password to establish a" +
                " connection with local PostgreSQL server");
    }

    private static String user = Main.getUserInputReader().askString("Enter user name");
    private static String password = Main.getUserInputReader().askString("Enter password");

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

