public class book {

    private String name, author , isbn;

    public book( String name , String author , String isbn){
        this.name = name;
        this.author = author;
        this.isbn = isbn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return name +"  "+ author+"  "+isbn;
    }
}
