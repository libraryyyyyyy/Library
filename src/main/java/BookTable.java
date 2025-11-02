import java.util.ArrayList;
import java.util.List;

public class BookTable {

    private List<book> books = new ArrayList<>();

    public void addBook( book b){
        books.add(b);
    }
    public List<book> searchBook( String keyword){

        List<book> res = new ArrayList<>();

        String edited_keyword = keyword.toLowerCase();

        for( book b : books){
            if( b.getName().toLowerCase().contains(edited_keyword)
            || b.getAuthor().toLowerCase().contains(edited_keyword)
            || b.getIsbn().toLowerCase().contains(edited_keyword)){
                res.add(b);
            }
        }
        return res;
    }

}
