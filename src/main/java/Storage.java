
import javafx.util.Pair;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Storage {

    private DBConnection dbc = new DBConnection();
    private Connection serverConnection = dbc.serverConnect();
    private Connection dbConnection;

    //Метод подготовки строки к использованию в  SQL запросе.
    private String concatenate(String str) {

        String regex = "'";
        str = str.replaceAll(regex, "''");
        str = "'" + str + "'";
        return str;
    }

    // Метод создания базы данных.
    void createDatabase() throws SQLException {
        String createDb = "CREATE DATABASE \"BOOK_SHOP_PROJECT\"";
        Statement statement = serverConnection.createStatement();
        statement.execute(createDb);
        statement.close();
        System.out.println("Database BOOK_SHOP_PROJECT created");
    }

    //Метод проверки наличия необходимой базы данных.
    boolean dbExists() {
        boolean exists = true;
        try {
            String sql = "SELECT * FROM pg_catalog.pg_database WHERE datname = \"BOOK_SHOP_PROJECT\"";
            Statement statement = serverConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            exists = false;
        }
        return exists;
    }

    //Метод проверки наличия юзера с соответствующим логином.
    boolean userExists(String login) {
        boolean exists = true;
        dbConnection = dbc.dbConnect();
        try {
            Statement statement = dbConnection.createStatement();
            String sql = "SELECT * FROM \"USERS\" WHERE lower(login) ="
                    + concatenate(login.toLowerCase());
            statement.executeQuery(sql);
        } catch (SQLException e) {
            exists = false;
        }
        return exists;
    }

    //Метод создания таблиц, если они ещё не созданы.
    void createTables() throws SQLException {
        String createHomeLibrary = "CREATE TABLE IF NOT EXISTS \"HOME_LIBRARY\"(\n" +
                "    id SERIAL NOT NULL,\n" +
                "    bookId bigint NOT NULL,\n" +
                "    quantity bigint NOT NULL,\n" +
                "    userId bigint NOT NULL,\n" +
                "    CONSTRAINT \"HOME_LIBRARY_pkey\" PRIMARY KEY (id)\n" +
                ")";
        String createShopDepository = "CREATE TABLE IF NOT EXISTS \"SHOP_DEPO\"(\n" +
                "    id SERIAL NOT NULL,\n" +
                "    author character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    title character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    publisher character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    year bigint NOT NULL,\n" +
                "    pages bigint NOT NULL,\n" +
                "    price double precision NOT NULL,\n" +
                "    quantity bigint NOT NULL,\n" +
                "    CONSTRAINT \"SHOP_DEPO_pkey\" PRIMARY KEY (id)\n" +
                ")";
        String createUsersTable = "CREATE TABLE IF NOT EXISTS \"USERS\"(\n" +
                "    id SERIAL NOT NULL,\n" +
                "    login character varying COLLATE pg_catalog.\"default\" NOT NULL UNIQUE,\n" +
                "    password character varying COLLATE pg_catalog.\"default\" NOT NULL UNIQUE,\n" +
                "    money double precision NOT NULL,\n" +
                "    CONSTRAINT \"USERS_pkey\" PRIMARY KEY (id)\n" +
                ")";
        String createOrdersTable = "CREATE TABLE IF NOT EXISTS \"ORDERS\"(\n" +
                "    id SERIAL NOT NULL,\n" +
                "    userId bigint NOT NULL,\n" +
                "    sum double precision NOT NULL,\n" +
                "    isPaid boolean NOT NULL,\n" +
                "    CONSTRAINT \"ORDERS_pkey\" PRIMARY KEY (id)\n" +
                ")";
        String createOrdersPositionsTable = "CREATE TABLE IF NOT EXISTS \"ORDERS_POSITIONS\"(\n" +
                "    id SERIAL NOT NULL,\n" +
                "    orderId bigint NOT NULL,\n" +
                "    bookId bigint NOT NULL,\n" +
                "    quantity bigint NOT NULL,\n" +
                "    CONSTRAINT \"ORDERS_POSITIONS_pkey\" PRIMARY KEY (id)\n" +
                ")";
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement();
        statement.execute(createHomeLibrary);
        statement.execute(createShopDepository);
        statement.execute(createUsersTable);
        statement.execute(createOrdersTable);
        statement.execute(createOrdersPositionsTable);
        statement.close();
    }

    // Метод заполнения магазина и домашней библиотеки базовым набором книг.
    void fillTables() {

        try {
            String fillLibrary = "INSERT INTO \"HOME_LIBRARY\" (bookId, quantity, userId)\n" +
                    " VALUES (1, 4, 1),\n" +
                    " (3, 5, 1), \n" +
                    " (5, 9, 1)";

            String fillShop = "INSERT INTO \"SHOP_DEPO\" (author, title, publisher, year, pages, price, quantity)\n" +
                    " VALUES ('King Stephen', 'Cujo', 'Viking Press'," +
                    " '1991', '319', '453', 10),\n" +
                    " ('Schildt Herbert', 'Java: A beginner''s Guide', 'Oracle Press'," +
                    " '2002', '602', '1800', 10),\n" +
                    " ('Schildt Herbert', 'Java: A Complete Reference', 'Oracle Press'," +
                    " '1996', '712', '3600', 10),\n" +
                    " ('Tolkien John Ronald Reuel', 'The Hobbit or There and back Again', 'HM'," +
                    " '1937', '313', '352', 10),\n" +
                    " ('Tolkien John Ronald Reuel', 'Leaf by Niggle', 'Newbook'," +
                    " '1945', '252', '300', 10),\n" +
                    " ('Tolkien John Ronald Reuel', 'The Fellowship of the Ring', 'George Allen & Unwin'," +
                    " '1954', '423', '425', 10),\n" +
                    " ('Tolkien John Ronald Reuel', 'The Two Towers', 'George Allen & Unwin'," +
                    " '1954', '352', '425', 10), \n" +
                    " ('Tolkien John Ronald Reuel', 'Return of the King', 'George Allen & Unwin'," +
                    " '1955', '416', '425', 10), \n" +
                    " ('Harrison Harry', 'Deathworld', 'Harry & Co'," +
                    " '1960', '320', '270', 10),\n" +
                    " ('King Stephen', 'Shining', 'Doubleday'," +
                    " '1977', '447', '549', 10)";
            dbConnection = dbc.dbConnect();
            Statement statement = dbConnection.createStatement();
            statement.execute(fillLibrary);
            statement.execute(fillShop);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Метод перевода результата запроса в список книг.
    private List<Book> resultToBooksList(ResultSet resultSet) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            String author = resultSet.getString("author");
            String title = resultSet.getString("title");
            String publisher = resultSet.getString("publisher");
            int year = resultSet.getInt("year");
            int pages = resultSet.getInt("pages");
            double price = resultSet.getDouble("price");
            int quantity = resultSet.getInt("quantity");
            Book book = new Book(author, title, publisher, year, pages, price);
            book.setQuantity(quantity);
            books.add(book);
        }
        return books;
    }

    // Метод чтения книг из базы данных в список.
    List<Book> readBooksFromTable(String table) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        ResultSet resultSet;
        List<Book> books = new ArrayList<>();

        switch (table) {
            case ("\"SHOP_DEPO\""):
                resultSet = statement.executeQuery("SELECT * FROM " + table);
                books = resultToBooksList(resultSet);
                break;
            case ("\"HOME_LIBRARY\""):
                String sql = "SELECT bookId, quantity FROM \"HOME_LIBRARY\" WHERE userId = "
                        + Main.getCurrentUser().getId();
                resultSet = statement.executeQuery(sql);
                List<Integer> indexes = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                while (resultSet.next()) {
                    indexes.add(resultSet.getInt("bookId"));
                    quantities.add(resultSet.getInt("quantity"));
                }
                StringBuilder result = new StringBuilder();
                for (Integer i : indexes) {
                    if (result.length() > 0) {
                        result.append(", ");
                    }
                    result.append(i);
                }
                sql = "SELECT * FROM \"SHOP_DEPO\" WHERE id IN (" +
                        result + ")";
                resultSet = statement.executeQuery(sql);
                books = resultToBooksList(resultSet);

                int i = 0;
                for (Book b : books) {

                    b.setQuantity(quantities.get(i));
                    i++;
                }
                break;
        }

        statement.close();

        return books;
    }

    //Метод внесения книг в базу данных.
    void addBookToTable(List<Book> list, String table) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

        for (Book book : list) {
            String write = "SELECT * FROM " + table + " WHERE author = " + concatenate(book.getAuthor()) +
                    " AND title = " + concatenate(book.getTitle()) +
                    " AND publisher = " + concatenate(book.getPublisher()) +
                    " AND year = " + book.getYear() +
                    " AND pages = " + book.getPages() +
                    " AND price = " + book.getPrice();
            ResultSet resultSet = statement.executeQuery(write);
            if (resultSet.next()) {
                int currentQuantity = resultSet.getInt("quantity");
                write = "UPDATE " + table + " SET quantity = " + (currentQuantity + book.getQuantity()) +
                        " WHERE title = " + concatenate(resultSet.getString("title"));
                statement.execute(write);
            } else {
                write = "INSERT INTO " + table + " (author, title, publisher, year, pages, price, quantity) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = dbConnection.prepareStatement(write);
                preparedStatement.setString(1, book.getAuthor());
                preparedStatement.setString(2, book.getTitle());
                preparedStatement.setString(3, book.getPublisher());
                preparedStatement.setInt(4, book.getYear());
                preparedStatement.setInt(5, book.getPages());
                preparedStatement.setDouble(6, book.getPrice());
                preparedStatement.setInt(7, book.getQuantity());
                preparedStatement.executeUpdate();
                preparedStatement.close();
                resultSet.close();

            }
        }
        statement.close();

    }

    //Метод удаления книги из базы данных.
    void removeBookFromTable(Book book, String table) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

        String removeBook = "SELECT * FROM " + table + " WHERE author = " + concatenate(book.getAuthor()) +
                " AND title = " + concatenate(book.getTitle()) +
                " AND publisher = " + concatenate(book.getPublisher()) +
                " AND year = " + book.getYear() +
                " AND pages = " + book.getPages() +
                " AND price = " + book.getPrice();
        ResultSet resultSet = statement.executeQuery(removeBook);
        resultSet.next();
        int currentQuantity = resultSet.getInt("quantity");
        if (book.getQuantity() > currentQuantity) {
            book.setQuantity(currentQuantity);
        }
        removeBook = "UPDATE " + table + " SET quantity = " + (currentQuantity - book.getQuantity()) +
                " WHERE title = " + concatenate(resultSet.getString("title"));
        statement.execute(removeBook);
        statement.close();
    }

    //Метод получения отсортированного списка книг из базы данных.
    List<Book> sortBooksInTable(String criteria, String table) throws SQLException {
        List<Book> books = new ArrayList<>();
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet;
        switch (table) {
            case ("\"SHOP_DEPO\""): {
                String sql = "SELECT * FROM " + table + " ORDER BY " +
                        criteria + " ASC";
                resultSet = statement.executeQuery(sql);
                books = resultToBooksList(resultSet);
                break;
            }

            case ("\"HOME_LIBRARY\""): {
                String sql = "SELECT bookId, quantity FROM \"HOME_LIBRARY\" WHERE userId = "
                        + Main.getCurrentUser().getId();
                resultSet = statement.executeQuery(sql);
                List<Integer> indexes = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                while (resultSet.next()) {
                    indexes.add(resultSet.getInt("bookId"));
                    quantities.add(resultSet.getInt("quantity"));
                }
                StringBuilder result = new StringBuilder();
                for (Integer i : indexes) {
                    if (result.length() > 0) {
                        result.append(", ");
                    }
                    result.append(i);
                }
                sql = "SELECT * FROM \"SHOP_DEPO\" WHERE id IN (" +
                        result + ")";
                resultSet = statement.executeQuery(sql);
                books = resultToBooksList(resultSet);

                int i = 0;
                for (Book b : books) {

                    b.setQuantity(quantities.get(i));
                    i++;
                }
                break;
            }
        }

        return books;
    }

    //Метод поиска книги в базе данных
    List<Book> searchTableForBook(String author, String title, String table) throws SQLException, IOException {
        dbConnection = dbc.dbConnect();
        List<Book> found = new ArrayList<>();
        Statement statement = dbConnection.createStatement();
        String sql;

        switch (table) {
            case ("\"SHOP_DEPO\""): {
                sql = "SELECT * FROM " + table +
                        " WHERE LOWER(author) = " + concatenate(author.toLowerCase()) + " AND LOWER(title) = " +
                        concatenate(title.toLowerCase());
                ResultSet resultSet = statement.executeQuery(sql);
                found = resultToBooksList(resultSet);
                break;
            }

            case ("\"HOME_LIBRARY\""): {
                List<Integer> bookIds = new ArrayList<>();
                sql = "SELECT * FROM \"SHOP_DEPO\" WHERE LOWER(author) = " +
                        concatenate(author.toLowerCase()) + " AND LOWER(title) = " +
                        concatenate(title.toLowerCase());
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    bookIds.add(resultSet.getInt("id"));
                }
                StringBuilder ids = new StringBuilder();
                for (Integer i : bookIds) {
                    if (ids.length() > 0) {
                        ids.append(", ");
                    }
                    ids.append(i);
                }
                sql = "SELECT bookId, quantity FROM \"HOME_LIBRARY\" WHERE userId = "
                        + Main.getCurrentUser().getId() + " AND bookId IN (" +
                        ids + ")";
                resultSet = statement.executeQuery(sql);

                List<Integer> indexes = new ArrayList<>();
                List<Integer> quantities = new ArrayList<>();
                while (resultSet.next()) {
                    indexes.add(resultSet.getInt("bookId"));
                    quantities.add(resultSet.getInt("quantity"));
                }
                StringBuilder idsTwo = new StringBuilder();
                for (Integer i : indexes) {
                    if (idsTwo.length() > 0) {
                        idsTwo.append(", ");
                    }
                    idsTwo.append(i);
                }
                sql = "SELECT * FROM \"SHOP_DEPO\" WHERE id IN (" +
                        idsTwo + ")";
                resultSet = statement.executeQuery(sql);
                found = resultToBooksList(resultSet);

                int i = 0;
                for (Book b : found) {
                    b.setQuantity(quantities.get(i));
                    i++;
                }
                break;
            }
        }
        if (found.size() == 0) {
            throw new IOException();
        }
        return found;
    }

    //Метод внесения пользователя в базу данных.
    void addUserToTable(User user) throws SQLException {
        dbConnection = dbc.dbConnect();

        String write = "INSERT INTO \"USERS\" (login, password, money) " +
                "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(write);
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setDouble(3, user.getMoney());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    // Метод чтения данных пользователя из базы данных.
    User readUserFromTable(String login) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM \"USERS\" WHERE lower(login) ="
                + concatenate(login.toLowerCase()));
        resultSet.next();
        User user = new User(resultSet.getString("login"));
        user.setId(resultSet.getInt("id"));
        user.setPassword(resultSet.getString("password"));
        user.setMoney(resultSet.getDouble("money"));
        statement.close();
        return user;
    }

    //Метод удаления пользователя из базы данных.
    void removeUserFromTable(String login) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement();
        String removeUser = "DELETE * FROM \"USERS\" WHERE lower(login) ="
                + concatenate(login.toLowerCase());
        statement.execute(removeUser);
        statement.close();
    }

    //Метод размещения заказа в базе данных.
    void addOrderToTable(Order order) throws SQLException {
        List<Book> books = order.getBooks();
        Statement statement = dbConnection.createStatement();
        String addOrder = "INSERT INTO \"ORDERS\" (userId, sum, isPaid) " +
                "VALUES (?, ?, ?) RETURNING id";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(addOrder, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, order.getUser().getId());
        preparedStatement.setDouble(2, order.getSum());
        preparedStatement.setBoolean(3, false);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int orderId = resultSet.getInt(1);

        for (Book book : books) {
            String sql = "SELECT * FROM \"SHOP_DEPO\" WHERE author ="
                    + concatenate(book.getAuthor()) +
                    " AND title =" + concatenate(book.getTitle()) +
                    " AND publisher =" + concatenate(book.getPublisher()) +
                    " AND year =" + book.getYear() +
                    " AND pages =" + book.getPages() +
                    " AND price =" + book.getPrice();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            int bookId = resultSet.getInt("id");
            int quantity = resultSet.getInt("quantity");


            if (book.getQuantity() > resultSet.getInt("quantity")) {
                System.out.println("Not enough books in shop depository");
                throw new SQLException();
            }

            String addBookId = "INSERT INTO \"ORDERS_POSITIONS\" (orderId, bookId, quantity) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement addOrderPositionsStatement = dbConnection.prepareStatement(addBookId);
            addOrderPositionsStatement.setInt(1, orderId);
            addOrderPositionsStatement.setInt(2, bookId);
            addOrderPositionsStatement.setInt(3, book.getQuantity());
            addOrderPositionsStatement.executeUpdate();

            String updateQuantity = "UPDATE \"SHOP_DEPO\" SET quantity =" +
                    (quantity - book.getQuantity()) +
                    " WHERE id =" + bookId;
            statement.executeUpdate(updateQuantity);
        }
        statement.close();
        preparedStatement.close();
    }

    //Метод удаления всех заказов пользователя из базы данных.
    void removeUsersOrdersFromTables(String login) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement();
        ResultSet userId = statement.executeQuery("SELECT * FROM \"USERS\" WHERE" +
                " (login) =" + concatenate(login));
        ResultSet orderId = statement.executeQuery("SELECT * FROM \"ORDERS\" WHERE" +
                " (userId) =" + userId.getInt("id"));

        String removeOrder = "DELETE * FROM \"ORDERS\" WHERE (id) =" + orderId.getInt("id");
        statement.execute(removeOrder);
        removeOrder = "DELETE * FROM \"ORDERS_POSITIONS\" WHERE (orderid) =" + orderId.getInt("id");
        statement.execute(removeOrder);
        statement.close();
    }

    //Метод увдаления отдельного заказа из базы данных.
    void removeOrderFromTables(Order order, boolean restoreQuantity) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement();
        String sql = "SELECT * FROM \"USERS\" WHERE lower(login) =" +
                concatenate(order.getUser().getLogin().toLowerCase());
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        int id = resultSet.getInt("id");
        sql = "SELECT * FROM \"ORDERS\" WHERE" +
                " (userId) =" + id + " AND (id) =" + order.getId();
        resultSet = statement.executeQuery(sql);
        resultSet.next();
        id = resultSet.getInt("id");
        sql = "DELETE FROM \"ORDERS\" WHERE (id) =" + id;
        statement.execute(sql);
        sql = "DELETE FROM \"ORDERS_POSITIONS\" WHERE (orderid) =" + id;
        statement.execute(sql);


        if (restoreQuantity) {
            for (Book book : order.getBooks()) {
                sql = "SELECT * FROM \"SHOP_DEPO\" WHERE author ="
                        + concatenate(book.getAuthor()) +
                        " AND title =" + concatenate(book.getTitle()) +
                        " AND publisher =" + concatenate(book.getPublisher()) +
                        " AND year =" + book.getYear() +
                        " AND pages =" + book.getPages() +
                        " AND price =" + book.getPrice();
                resultSet = statement.executeQuery(sql);
                resultSet.next();
                sql = "UPDATE \"SHOP_DEPO\" SET quantity =" +
                        (resultSet.getInt("quantity") + book.getQuantity()) +
                        " WHERE id =" + resultSet.getInt("id");
                statement.executeUpdate(sql);
            }
        }
        statement.close();
    }

    // Метод чтения заказов пользователя из базы данных в список.
    List<Order> readOrdersFromTable(String login) throws SQLException {
        dbConnection = dbc.dbConnect();

        List<Order> orders = new ArrayList<>();
        List<Book> books;
        List<Integer> booksIds = new ArrayList<>();
        Map<Integer, Pair<Double, Boolean>> idsAndStatus = new HashMap<>();
        User user = readUserFromTable(login);
        Order order;

        Statement statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

        String sql = "SELECT * FROM \"USERS\" WHERE lower(login) =" +
                concatenate(login).toLowerCase();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        int userId = resultSet.getInt("id");
        sql = "SELECT * FROM \"ORDERS\" WHERE (userId) =" +
                userId;
        resultSet = statement.executeQuery(sql);


        while (resultSet.next()) {
            Pair<Double, Boolean> sumsAndIsPaid = new Pair<>(resultSet.getDouble("sum"),
                    resultSet.getBoolean("isPaid"));
            idsAndStatus.put(resultSet.getInt("id"), sumsAndIsPaid);
        }
        resultSet.beforeFirst();


        for (Integer i : idsAndStatus.keySet()) {
            sql = "SELECT * FROM \"ORDERS_POSITIONS\" " +
                    "WHERE orderId = " + i;
            resultSet = statement.executeQuery(sql);

            List<Integer> quantities = new ArrayList<>();

            while (resultSet.next()) {
                quantities.add(resultSet.getInt("quantity"));
            }

            resultSet.beforeFirst();

            while (resultSet.next()) {
                booksIds.add(resultSet.getInt("bookId"));
            }

            StringBuilder result = new StringBuilder();
            for (Integer b : booksIds) {
                if (result.length() > 0) {
                    result.append(", ");
                }
                result.append(b);
            }

            sql = "SELECT * FROM \"SHOP_DEPO\" WHERE id IN (" +
                    result + ")";
            resultSet = statement.executeQuery(sql);
            books = resultToBooksList(resultSet);
            int j = 0;
            for (Book b : books) {
                b.setQuantity(quantities.get(j));
                j++;
            }
            order = new Order(user, books);
            order.setId(i);
            order.setSum(idsAndStatus.get(i).getKey());
            order.setPaid(idsAndStatus.get(i).getValue());
            orders.add(order);
            booksIds.clear();
        }

        resultSet.close();
        statement.close();
        return orders;
    }

    //Метод оплаты заказа текущего юзера.
    void payForOrder(Order order) throws SQLException {
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement();
        String updateIsPaid = "UPDATE \"ORDERS\" SET isPaid = true WHERE (id) =" + order.getId();
        statement.executeUpdate(updateIsPaid);
        statement.close();
    }

    //Метод отправки заказа в домашнюю библиотеку юзера.
    void shipOrder(Order order) throws SQLException {
        List<Book> books = order.getBooks();
        dbConnection = dbc.dbConnect();
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet;

        for (Book book : books) {
            String sql = "SELECT * FROM \"SHOP_DEPO\" WHERE author ="
                    + concatenate(book.getAuthor()) +
                    " AND title =" + concatenate(book.getTitle()) +
                    " AND publisher =" + concatenate(book.getPublisher()) +
                    " AND year =" + book.getYear() +
                    " AND pages =" + book.getPages() +
                    " AND price =" + book.getPrice();
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            int bookId = resultSet.getInt("id");

            String addBookId = "INSERT INTO \"HOME_LIBRARY\" (bookId, quantity, userid) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement addOrderPositionsStatement = dbConnection.prepareStatement(addBookId);
            addOrderPositionsStatement.setInt(1, bookId);
            addOrderPositionsStatement.setInt(2, book.getQuantity());
            addOrderPositionsStatement.setInt(3, order.getUser().getId());
            addOrderPositionsStatement.executeUpdate();
        }
        removeOrderFromTables(order, false);
        statement.close();
    }
}

/*    //Метод перевода результата запроса в список заказов.
    List<Order> resultToOrdersList(ResultSet resultSet, ResultSet resultSet) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(resultSet.getString("login"));
            String title = resultSet.getString("title");
            String publisher = resultSet.getString("publisher");
            int year = resultSet.getInt("year");
            int pages = resultSet.getInt("pages");
            double price = resultSet.getDouble("price");
            int quantity = resultSet.getInt("quantity");
            Book book = new Book(author, title, publisher, year, pages, price);
            book.setQuantity(quantity);
            orders.add(order);
        }
        return orders;
    }*/