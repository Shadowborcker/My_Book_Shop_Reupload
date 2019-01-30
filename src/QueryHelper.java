import java.sql.Connection;
import java.sql.Statement;

public class QueryHelper {
    BookFactory bookFactory = new BookFactory();
    UserInputReader userInputReader = new UserInputReader();
    Storage storage = new Storage();
    DBConnection dbConnection = new DBConnection();
    Connection connection = dbConnection.dbConnect();
    Connection connectionTwo = dbConnection.dbConnect();
}
