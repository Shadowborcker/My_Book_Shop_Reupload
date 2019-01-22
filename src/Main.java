
/*
Создаём книжный магазин.
Цель - приложение с консольным интерфейсом, хранящее книги в виде записей в файле на жестком диске.
Функционал условынй - добавление, удаление, обмен и покупка книг.
Работа со списком книг - сортировка, поиск.
*/

import java.io.*;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {

        // Запрашиваем логин и пароль, устанавливаем соединение с сервером PostgreSQL.
        System.out.println("Welcome to Black Books - a test project by Nikolay Akimov. \n" +
                "Application uses PostgreSQL to store it's data. \n" +
                "We require your PostgreSQL username and password to establish a" +
                " connection with local PostgreSQL server");
        DBConnection dbConnection = new DBConnection();
        Storage storage = new Storage();

        //Проверям наличие базы данных, в случае отсутствия создаём базу данных, создаём таблицы и заполняем их.
        try {
            if (!storage.exists(dbConnection.serverConnect())) {
                storage.createDatabase(dbConnection.serverConnect());
                storage.createTables(dbConnection.dbConnect());
                storage.fillTables(dbConnection.dbConnect());
            }
        } catch (SQLException e) {
            System.out.println("Database BOOK_SHOP_PROJECT already exists.");
        }


        //Отображаем меню и обрабатываем команды пользователя.
        Menu menu = new Menu();
        UserInputReader userInputReader = new UserInputReader();
        while (true) {
            try {
                menu.greetingsMenu();
                break;
            } catch (IOException e) {
                System.out.println("Invalid input");
                e.printStackTrace();
            }
        }

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



