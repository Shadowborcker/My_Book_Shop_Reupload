
/*
Создаём книжный магазин.
Финальная цель - Rest API с использованием PostgreSQL для хранения данных.
*/

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<Book> currentUserBasket = new ArrayList<>();
    private static User currentUser;
    private static UserInputReader userInputReader = new UserInputReader();
    private static BookFactory bookFactory = new BookFactory();
    private static Storage storage = new Storage();


    public static List<Book> getCurrentUserBasket() {
        return currentUserBasket;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static UserInputReader getUserInputReader() {
        return userInputReader;
    }

//    public static DBConnection getDbc() {
//        return dbc;
//    }

    public static BookFactory getBookFactory() {
        return bookFactory;
    }

    public static Storage getStorage() {
        return storage;
    }

//    public static Connection getDbConnection() {
//        return dbConnection;
//    }
//
//    public static Connection getServerConnection() {
//        return serverConnection;
//    }

    public static void setCurrentUser(User currentUser) {
        Main.currentUser = currentUser;
    }

    public static void main(String[] args) {

        // Запрашиваем логин и пароль, устанавливаем соединение с сервером PostgreSQL.
        System.out.println("Welcome to Black Books - a test project by Nikolay Akimov.");

        //Проверям наличие базы данных, в случае отсутствия создаём базу данных, создаём таблицы и заполняем их.
        try {
            if (!storage.exists()) {
                storage.createDatabase();
                storage.createTables();
                storage.fillTables();
            }
        } catch (SQLException e) {
            System.out.println("Database BOOK_SHOP_PROJECT already exists.");
        }


        //Отображаем меню и обрабатываем команды пользователя.
        Menu menu = new Menu();
        menu.greetingsMenu();
        while (true) {
            try {
                String item = userInputReader.askString("Input command");
                menu.chooseMenuItem(item);
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
        }
    }
}



