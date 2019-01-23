import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {

    // Метод создания базы данных.
    void createDatabase(Connection serverConnection) throws SQLException {
        String createDb = "CREATE DATABASE \"BOOK_SHOP_PROJECT\"";
        Statement statement = serverConnection.createStatement();
        statement.execute(createDb);
        statement.close();
        System.out.println("Database BOOK_SHOP_PROJECT created");
    }

    //Метод проверки наличия необходимой базы данных.
    boolean exists(Connection serverConnection) {
        boolean exists = true;
        try {
            String sql = "CREATE DATABASE \"BOOK_SHOP_PROJECT\"";
            Statement statement = serverConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            exists = false;
        }
        return exists;
    }

    //Метод создания таблиц, если они ещё не созданы.
    void createTables(Connection dbConnection) throws SQLException {
        String createHomeLibrary = "CREATE TABLE IF NOT EXISTS \"HOME_LIBRARY\"(\n" +
                "    id SERIAL NOT NULL,\n" +
                "    author character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    title character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    publisher character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
                "    year bigint NOT NULL,\n" +
                "    pages bigint NOT NULL,\n" +
                "    price double precision NOT NULL,\n" +
                "    quantity bigint NOT NULL,\n" +
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
                "    login character varying COLLATE pg_catalog.\"default\" NOT NULL,\n" +
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

        Statement statement = dbConnection.createStatement();
        statement.execute(createHomeLibrary);
        statement.execute(createShopDepository);
        statement.execute(createUsersTable);
        statement.execute(createOrdersTable);
        statement.execute(createOrdersPositionsTable);
        statement.close();
    }

    // Метод заполнения магазина и домашней библиотеки базовым набором книг.
    void fillTables(Connection dbConnection) {

        try {
            String fillLibrary = "INSERT INTO \"HOME_LIBRARY\" (author, title, publisher, year, pages, price, quantity)\n" +
                    " VALUES ('Tolkien John Ronald Reuel', 'The Fellowship of the Ring', 'George Allen & Unwin'," +
                    " '1954', '423', '425', 1),\n" +
                    " ('Tolkien John Ronald Reuel', 'The Two Towers', 'George Allen & Unwin'," +
                    " '1954', '352', '425', 1), \n" +
                    " ('Tolkien John Ronald Reuel', 'Return of the King', 'George Allen & Unwin'," +
                    " '1955', '416', '425', 1)";

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
    List<Book> readBooksFromTable(String table, Connection dbConnection) throws SQLException {
        List<Book> books;
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
        books = resultToBooksList(resultSet);
        resultSet.close();
        statement.close();

        return books;
    }

    //Метод внесения книг в базу данных.
    void addBookToTable(List<Book> list, String table, Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

        for (Book book : list) {
            String write = "SELECT * FROM " + table + " WHERE author = " + book.getAuthor() +
                    " AND title = " + book.getTitle() +
                    " AND publisher = " + book.getPublisher() +
                    " AND year = " + book.getYear() +
                    " AND pages = " + book.getPages() +
                    " AND price = " + book.getPrice();
            ResultSet resultSet = statement.executeQuery(write);
            if (resultSet.next()) {
                int currentQuantity = resultSet.getInt("quantity");
                write = "UPDATE " + table + " SET quantity = " + (currentQuantity + book.getQuantity()) +
                        " WHERE title = " + resultSet.getString("title");
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
    void removeBookFromTable(Book book, String table, Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE);

        String removeBook = "SELECT * FROM " + table + " WHERE author = " + book.getAuthor() +
                " AND title = " + book.getTitle() +
                " AND publisher = " + book.getPublisher() +
                " AND year = " + book.getYear() +
                " AND pages = " + book.getPages() +
                " AND price = " + book.getPrice();
        ResultSet resultSet = statement.executeQuery(removeBook);

        int currentQuantity = resultSet.getInt("quantity");
        if (book.getQuantity() > currentQuantity) {
            book.setQuantity(currentQuantity);
        }
        removeBook = "UPDATE " + table + " SET quantity = " + (currentQuantity - book.getQuantity()) +
                " WHERE title = " + resultSet.getString("title");

/*        String removeBook = "DELETE * FROM " + table + " WHERE LOWER(author) = " + book.getAuthor().toLowerCase() +
                " AND LOWER(title) = " + book.getTitle().toLowerCase() +
                " AND LOWER(publisher) = " + book.getPublisher().toLowerCase() +
                " AND year = " + book.getYear() +
                " AND pages = " + book.getPages() +
                " AND price = " + book.getPrice();*/
        statement.execute(removeBook);
        statement.close();
    }

    //Метод получения отсортированного списка книг из базы данных.
    List<Book> sortBooksInTable(String criteria, String table, Connection dbConnection) throws SQLException {
        List<Book> books;
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table + " ORDER BY " + criteria +
                " DESC");
        books = resultToBooksList(resultSet);
        resultSet.close();
        statement.close();

        return books;
    }

    //Метод поиска книги в базе данных
    List<Book> searchTableForBook(String author, String title, String table, Connection dbConnection)
            throws SQLException {
        List<Book> found;
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table +
                " WHERE LOWER(author) = " + author.toLowerCase() + " AND LOWER(title) = " + title.toLowerCase());
        found = resultToBooksList(resultSet);
        return found;
    }

    //Метод внесения пользователя в базу данных.
    void addUserToTable(User user, Connection dbConnection) throws SQLException {

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
    User readUserFromTable(String login, Connection dbConnection) throws SQLException {
        UserInputReader userInputReader = new UserInputReader();
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM \"USERS\" WHERE (login) = " + login);
        String password;
        User user = new User(resultSet.getString("login"), resultSet.getString("password"),
                resultSet.getDouble("money"));
        statement.close();
        return user;
    }

    //Метод удаления пользователя из базы данных.
    void removeUserFromTable(String login, Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement();
        String removeUser = "DELETE * FROM \"USERS\" WHERE (login) = " + login;
        statement.execute(removeUser);
        statement.close();
    }

    //Метод размещения заказа в базе данных.
    void addOrderToTable(Order order, Connection dbConnection) throws SQLException {
        List<Book> books = order.getBooks();
        String login = order.getUser().getLogin();
        Statement statement = dbConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM \"USERS\" WHERE (login) = " + login);
        String addOrder = "INSERT INTO \"ORDERS\" (userId, sum, isPaid) " +
                "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = dbConnection.prepareStatement(addOrder);
        preparedStatement.setInt(1, resultSet.getInt("id"));
        preparedStatement.setDouble(2, order.getSum());
        preparedStatement.setBoolean(3, false);
        preparedStatement.executeUpdate();

        for (Book book: books) {
            ResultSet bookId = statement.executeQuery("SELECT * FROM \"SHOP_DEPO\" WHERE author = "
                    + book.getAuthor() +
                    " AND title = " + book.getTitle() +
                    " AND publisher = " + book.getPublisher() +
                    " AND year = " + book.getYear() +
                    " AND pages = " + book.getPages() +
                    " AND price = " + book.getPrice());

            if (book.getQuantity()> bookId.getInt("quantity")) {
                System.out.println("Not enough books in shop depository");
                throw new SQLException();
            }
            ResultSet orderId = statement.executeQuery("SELECT * FROM \"ORDERS\" WHERE" +
                    " (userId) = " + resultSet.getInt("id"));

            String addBookId = "INSERT INTO \"ORDERS_POSITIONS\" (orderId, bookId, quantity) " +
                    "VALUES (?, ?, ?)";
            PreparedStatement addOrderPositionsStatement = dbConnection.prepareStatement(addBookId);
            addOrderPositionsStatement.setInt(1, orderId.getInt("id"));
            addOrderPositionsStatement.setInt(2, bookId.getInt("id"));
            addOrderPositionsStatement.setInt(3, book.getQuantity());
            addOrderPositionsStatement.executeUpdate();

            String updateQuantity = "UPDATE \"SHOP_DEPO\" SET quantity = " +
                    (bookId.getInt("quantity") - book.getQuantity()) +
                    " WHERE id = " + bookId.getInt("id");
            statement.executeUpdate(updateQuantity);
        }
        statement.close();
        preparedStatement.close();
    }

    //Метод удаления заказа из базы данных.
    void removeOrderFromTables(String login, Connection dbConnection) throws SQLException {
        Statement statement = dbConnection.createStatement();
        ResultSet userId = statement.executeQuery("SELECT * FROM \"USERS\" WHERE" +
                " (login) = " + login);
        ResultSet orderId = statement.executeQuery("SELECT * FROM \"ORDERS\" WHERE" +
                " (userId) = " + userId.getInt("id"));

        String removeOrder = "DELETE * FROM \"ORDERS\" WHERE (id) = " + orderId.getInt("id");
        statement.execute(removeOrder);
        removeOrder = "DELETE * FROM \"ORDERS_POSITIONS\" WHERE (orderid) = " + orderId.getInt("id");
        statement.close();
    }


    // Метод чтения заказов пользователя из базы данных в список.
    List<Order> readOrdersFromTable(User user, Connection dbConnection) throws SQLException {
        Order order;
        List<Order> orders = new ArrayList<>();
        List<Book> books;
        Statement statement = dbConnection.createStatement();
        ResultSet resultSetUsers = statement.executeQuery("SELECT * FROM \"USERS\" WHERE (login) = " +
                user.getLogin());
        ResultSet resultSetOrders = statement.executeQuery("SELECT * FROM \"ORDERS\" WHERE (userId) = " +
                resultSetUsers.getInt("id"));
        ResultSet resultSetPositions;
        ResultSet booksOrdered;

        while (resultSetOrders.next()) {
            resultSetPositions = statement.executeQuery("SELECT * FROM \"ORDERS_POSITIONS\" " +
                    "WHERE (orderId) = " + resultSetOrders.getInt("id"));
            booksOrdered = statement.executeQuery("SELECT * FROM \"SHOP_DEPO\" " +
                    "WHERE (id) = " + resultSetPositions.getInt("bookId"));
            books = resultToBooksList(booksOrdered);
            order = new Order(user,books);
            orders.add(order);
        }
        resultSetOrders.close();
        statement.close();

        return orders;
    }
}

/*    //Метод перевода результата запроса в список книг.
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