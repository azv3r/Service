import java.util.ArrayList;
import java.util.List;

public interface DAO {

    public void insert(String tableToInsert, List<String> values) throws Exception;
    public void update(String table, String id, ArrayList<String> values) throws Exception;
    public ArrayList<List<String>> getData(String table) throws Exception;

}
