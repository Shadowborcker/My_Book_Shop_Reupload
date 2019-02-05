
class BookFactory {
    private UserInputReader reader = new UserInputReader();

    Book newBook() {

        String name = reader.askString("Enter author's name");
        String surname = reader.askString("Enter author's surname");
        String author = (surname + " " + name);
        String title = reader.askString("Enter title");
        String publisher = reader.askString("Enter publisher");
        int year = reader.askYear("Enter the year of publishing");
        int pages = reader.askInt("Enter number of pages");
        double price = reader.askDouble("Enter pricetag");
        return new Book(author, title, publisher, year, pages, price);

    }
}
