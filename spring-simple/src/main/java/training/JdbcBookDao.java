package training;

public class JdbcBookDao implements BookDao {

    @Override
    public void saveBook(String author, String title) {
        System.out.println("JDBC");
    }
}
