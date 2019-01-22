import java.io.IOException;
import java.sql.SQLException;

interface MenuItem {
    String description();

    void select() throws IOException, SQLException;
}
