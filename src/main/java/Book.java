import java.util.Objects;
import java.text.DecimalFormat;

public class Book {

    private String author;
    private String title;
    private String publisher;
    private int year;
    private int pages;
    private double price;
    private int quantity;
    private int id;

    String getAuthor() {
        return author;
    }

    String getTitle() {
        return title;
    }

    String getPublisher() {
        return publisher;
    }

    int getYear() {
        return year;
    }

    int getPages() {
        return pages;
    }

    double getPrice() {
        return price;
    }

    int getQuantity() {
        return quantity;
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Переопределяем equals и hashCode для сравнения книг, поле количество не учитывается.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(author.toLowerCase(), book.author.toLowerCase()) &&
                Objects.equals(title.toLowerCase(), book.title.toLowerCase());
    }

        @Override
    public int hashCode() {
        return Objects.hash(author.toLowerCase(), title.toLowerCase());
    }

    // Переопределяем toString для вывода на экран.
    @Override
    public String toString() {
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        return "Book{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", year=" + year +
                ", pages=" + pages +
                ", price=" + numberFormat.format(price) +
                ", quantity=" + quantity +
                '}';
    }

    // Полный конструктор.
    public Book(String author, String title, String publisher, int year, int pages, double price) {
        this.author = author;
        this.title = title;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.price = price;
    }

}
