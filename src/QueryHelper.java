import java.sql.Connection;

public class QueryHelper {
    BookFactory bookFactory = new BookFactory();
    UserInputReader userInputReader = new UserInputReader();
    Storage storage = new Storage();
    private DBConnection dbConnection = new DBConnection();
    Connection connection = dbConnection.dbConnect();
}
