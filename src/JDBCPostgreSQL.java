import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCPostgreSQL implements DAO{

    private String url = "jdbc:postgresql://127.0.0.1:5432";
    private String user = "root";
    private String password = "asdd";

    private Connection con;
    private Statement stmt;
    private ResultSet rs;

    public JDBCPostgreSQL(String connection, String username, String pass) throws SQLException {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(connection, username, pass);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e);
        }

        catch (SQLException e)
        {
            throw e;
        }
    }

    public JDBCPostgreSQL() throws SQLException {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println(e);
        }
        catch (SQLException e)
        {
            throw e;
        }
    }

    public int findNewId(String table) throws SQLException {
        String request = "SELECT * from " + table;
        Statement st;
        this.rs = null;
        int ans = -1;
        try
        {
            st = con.createStatement();
            rs = st.executeQuery(request);
            while(rs.next())
            {
                ArrayList<String> list = new ArrayList<String>();

                int b = Integer.parseInt(rs.getString(1));
                ans = ans > b ? ans : b;
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            throw e;
        }

        return ans;
    }
    @Override
    public void insert(String tableToInsert, List<String> values) throws SQLException
    {
        try
        {
            this.stmt = con.createStatement();
            String v = "";

            for(String s : values)
                v += "'" + s + "',";

            v = v.substring(0, v.length() - 1);
            System.out.println(v);
            stmt.executeUpdate("INSERT INTO " + tableToInsert + " VALUES(" + v + ")");
        }
        catch (SQLException e)
        {
            throw e;
        }
    }

    public void insert(String table, String title, String filepath) throws SQLException {

        try
        {
            this.stmt = con.createStatement();
            String v = "";
            if(table == "songs") {
                v += Integer.toString(findNewId(table) + 1) + ",'" + title + "'," + "null, " + "null," + "'" + filepath + "'";
            }
            else if(table == "artist")
            {
                v += Integer.toString(findNewId(table) + 1) + ",'" + title + "'";
            }
            else
            {
                v += Integer.toString(findNewId(table) + 1) + ",'" + title + "'," + "null";
            }

            //System.out.println(v);
            stmt.executeUpdate("INSERT INTO " + table + " VALUES(" + v + ")");

        }catch (SQLException e)
        {
            throw e;
        }
    }

    @Override
    public void update(String table, String title, ArrayList<String> valuesToSet) throws Exception
    {
        String request = "";


        try
        {
            ResultSetMetaData rsmd = con.createStatement().executeQuery("SELECT * FROM " + table).getMetaData();

            request = "UPDATE " + table + " SET " + valuesToSet.get(0) + "=" + valuesToSet.get(1) + "WHERE " + title + "=" + rsmd.getColumnName(1);

            con.prepareStatement(request).execute();
        }
        catch (Exception e)
        {
            System.out.println(request);
            throw e;
        }
    }

    @Override
    public ArrayList<List<String>> getData(String table) throws Exception
    {
        ArrayList<List<String>> ans = new ArrayList<List<String>>();
        String request = "SELECT * from " + table;
        Statement st;
        this.rs = null;

        try
        {
            st = con.createStatement();
            rs = st.executeQuery(request);
            int temp = 0;

            while(rs.next())
            {
                ArrayList<String> list = new ArrayList<String>();
                if(table.equals("songs"))   temp = 4;
                else    throw new Exception("Wrong table");

                for(int i = 1;i <= temp;i++)
                    list.add(rs.getString(i));

                ans.add(list);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            throw e;
        }

        return ans;
    }

    public void createTable(String tableName) throws SQLException {
        Statement st = con.createStatement();
        String request = "CREATE TABLE";
        rs = st.executeQuery(request);
    }

    public String findInTable(String tableName, String title) throws SQLException {
        stmt = con.createStatement();
        String request;
        if(tableName == "songs") {
            request = "SELECT * FROM " + tableName + " WHERE title= '" + title + "'";
        }
        else if(tableName == "artist")
        {
            request = "SELECT * FROM " + tableName + " WHERE name= '" + title + "'";
        }
        else
        {
            request = "SELECT * FROM " + tableName + " WHERE title= '" + title + "'";
        }
        System.out.println(request);
        rs = stmt.executeQuery(request);
        if(!rs.next()) {
            rs.beforeFirst();
            return null;
        }
        else
        {
            String ans = "";

            if(tableName == "songs") {
                ans += "Title: " + rs.getString(1) + " duration: " + rs.getString(2) + " year: ";
                ans += rs.getString(3) + " id: " + rs.getString(4);

            }
            else if(tableName == "artist")
            {
                ans += "Name: " + rs.getString(1) + " id: " + rs.getString(2);

            }
            else
            {

            }
            rs.beforeFirst();
            return ans;
        }
    }

    public int delete(String titleOfSong, String table) throws SQLException {
        String temp = findInTable(table, titleOfSong);
        if(temp != null) {
            String req = "Delete from " + table + " Where title='" + titleOfSong + "'";
            Statement st = con.createStatement();
            st.executeUpdate(req);
            return 1;
        }
        else
            return 0;

    }
}

