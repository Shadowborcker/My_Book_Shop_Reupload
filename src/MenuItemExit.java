import java.sql.SQLException;

//Опция меню для выхода из программы.
class MenuItemExit extends QueryHelper implements MenuItem {
    public String description() {
        return "Terminating program.\n" +
                "See you again, sir!";
    }

    public void select() {
        System.out.println(description());
        try {
            connection.close();
        } catch (SQLException e) {
            System.exit(0);
        }
        System.exit(0);
    }

}
