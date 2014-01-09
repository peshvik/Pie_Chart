import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.sun.org.apache.xpath.internal.operations.Equals;


public class DBProvider {
	

	private static String dbURL = "jdbc:derby:PieChart;create=true";
    private static String tableName = "BD_PieChart";
    // jdbc Connection
	private static Connection conn = null;
	private static Statement stmt = null;
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

 
    
	public static void connect() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			// Get a connection
			conn = DriverManager.getConnection(dbURL);
			ResultSet tables = conn.getMetaData().getTables(null, null, null, null);
			boolean isFound = false;
			while (tables.next()) {
				String name = tables.getString("TABLE_NAME");
				if (name.equals(tableName))
				{
					isFound = true;
					break;
				}
			}
			
			if (!isFound)
			{
				stmt = conn.createStatement();
				stmt.execute("CREATE TABLE " + tableName + "(ID INTEGER, SYMBOL VARCHAR(20), DATE DATE, PRICE FLOAT)");
				stmt.close();
			}
			
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
	
    public static void createConnection()
    {
        try
        {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            //Get a connection
            conn = DriverManager.getConnection(dbURL); 
        }
        catch (Exception except)
        {
            except.printStackTrace();
        }
    }
    
  public static boolean checkingExistence(StockPrice stock) {
	  boolean key=false;
	  try {
			//stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from " + tableName + " where SYMBOL='" +stock.getSymbol()+ "' AND DATE='"+ dateFormat.format(stock.getDate()) + "' AND PRICE=" + stock.getPrice());
			key=results.next();
			results.close();
			//stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
  return key;
}
    
    public static void insertStock(StockPrice stock) {
		int id = 1;
		try {	
			if (!checkingExistence(stock)){
			String insertStmt = "insert into " + tableName + " values (" + id++ + ",'"
					+ stock.getSymbol() + "','" + dateFormat.format(stock.getDate()) + "'," + stock.getPrice() + ")";
			stmt = conn.createStatement();
			stmt.execute(insertStmt);
			stmt.close();
			}
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}
    
    
	public static void printStocks() {
		try {
			stmt = conn.createStatement();
			ResultSet results = stmt.executeQuery("select * from " + tableName);
			ResultSetMetaData rsmd = results.getMetaData();
			int numberCols = rsmd.getColumnCount();
			for (int i = 2; i <= numberCols; i++) {
				// print Column Names
				System.out.print(rsmd.getColumnLabel(i) + "\t\t\t");
			}

			System.out.println("\n-------------------------------------------------");

			while (results.next()) {
				String name = results.getString("SYMBOL");
				Date date = results.getDate("DATE");
				float price = results.getFloat("PRICE");
				System.out.println( name + "\t\t\t" + date + "\t\t\t" + price);
			}
			System.out.println("\n-------------------------------------------------\n\n");
			results.close();
			stmt.close();
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}
	
	public static void shutdown() {
		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				DriverManager.getConnection(dbURL + ";shutdown=true");
				conn.close();
			}
		} catch (SQLException sqlExcept) {
		}
	}
	
	public static void main(String[] args) {
		connect();
		List<StockPrice> data = YahooHelper.getData();
		for (StockPrice stockPrice : data) {
			insertStock(stockPrice);
		}
		printStocks();
		shutdown();
		System.out.println();
	}
	
}
