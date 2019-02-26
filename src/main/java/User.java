import java.util.Objects;

public class User {

    private int id;
    private String login;
    private String password;
    private double money;


    void setPassword(String password) {
        this.password = password;
    }

    void setMoney(double money) {
        this.money = money;
    }

    String getLogin() {
        return login;
    }

    String getPassword() {
        return password;
    }

    double getMoney() {
        return money;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    User(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", money=" + money +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.getMoney(), getMoney()) == 0 &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin(), getPassword(), getMoney());
    }
}
