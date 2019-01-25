import java.text.DecimalFormat;
import java.util.List;

public class Order {
    int id;
    User user;
    List<Book> books;
    boolean isPaid;
    double sum;

    public Order(User user, List<Book> books) {
        this.user = user;
        this.books = books;
        this.isPaid = false;
        for (Book b : books) {
            this.sum += b.getPrice();
        }
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public User getUser() {
        return user;
    }

    public List<Book> getBooks() {
        return books;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public double getSum() {
        return sum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String bookListToString(List<Book> books) {
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : books
        ) {
            stringBuilder.append(i + " " + book + "\n");
            i++;
        }
        return stringBuilder.toString();
    }

    public String isPaidToString(Boolean isPaid) {
        String status;
        if (isPaid) {
            status = "has been paid for";
        } else {
            status = "has not been paid for";
        }
        return status;
    }

    @Override
    public String toString() {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        return "Order{" +
                "order's owner: " + user.getLogin() + "\n" +
                "ordered positions: \n" + bookListToString(books) +
                "order " + isPaidToString(isPaid) + "\n" +
                "for a total sum of" + numberFormat.format(sum) + "RUB" +
                '}';
    }
}
