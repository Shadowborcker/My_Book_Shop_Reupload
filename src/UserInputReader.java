import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class UserInputReader {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // Метод запроса строки
    public String askString(String s) throws IOException {
        String string;
        while (true) {
            System.out.println(s);
                string = reader.readLine();
                if (string.isEmpty()) {
                    System.out.println("Invalid input.");
                } else break;
            }
        return string;
    }
    //Метод запроса целого числа
    public int askInt(String s) throws IOException {
        int i;
        System.out.println(s);
        while (true) {
            i = Integer.parseInt( reader.readLine());
            if (i <= 0) {
                System.out.println("Invalid input - minimum is 1.");
            }
            else break;
        }
        return i;
    }
    //Метод запроса дробного числа
    public double askDouble(String s) throws IOException {
        double i;
        System.out.println(s);
        while (true) {
            i = Double.parseDouble( reader.readLine());
            if (i <= 0) {
                System.out.println("Invalid input - minimum is 1.");
            }
            else break;
        }
        return i;
    }
    //Метод запроса числа
    public int askYear(String s) throws IOException {
        int i;
        while (true) {
            System.out.println(s);
                i = Integer.parseInt( reader.readLine());
                if (i <= 0 || i > Calendar.getInstance().get(Calendar.YEAR)) {
                    System.out.println("Invalid year.");
                }
                else break;
        }
        return i;
    }

}
