import java.text.DecimalFormat;
import java.util.List;

public class Order {
    private int id;
    private User user;
    List<Book> books;
    boolean isPaid;
    private double sum;

    Order(User user, List<Book> books) {
        this.user = user;
        this.books = books;
        this.isPaid = false;
        for (Book b : books) {
            this.sum += (b.getPrice() * b.getQuantity());
        }
    }

    void setPaid(boolean paid) {
        isPaid = paid;
    }

    User getUser() {
        return user;
    }

    public List<Book> getBooks() {
        return books;
    }

    boolean getIsPaid() {
        return isPaid;
    }

    double getSum() {
        return sum;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    private String bookListToString(List<Book> books) {
        int i = 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (Book book : books
        ) {
            stringBuilder.append(i + " " + book.toString() + "\n");
            i++;
        }
        return stringBuilder.toString();
    }

    private String isPaidToString(Boolean isPaid) {
        String status;
        if (isPaid) {
            status = "has been paid for";
        } else {
            status = "has not been paid for";
        }
        return status;
    }

//    @Override
//    public String toString() {
//        DecimalFormat numberFormat = new DecimalFormat("#.00");
//        return "Order{" +
//                "order's owner: " + user.getLogin() + "\n" +
//                "ordered positions: \n" + bookListToString(books) + "\n" +
//                "order " + isPaidToString(isPaid) + "\n" +
//                "for a total sum of" + numberFormat.format(sum) + "RUB" +
//                '}';
//    }
}
