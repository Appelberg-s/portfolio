import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GuestBookDatabase {
	
	private static final String USER_NAME = "usr_14615787";
	private static final String DB_NAME = "db_14615787";
	private static final String PASSWORD = "615787";
	private static final String COMPUTER_NAME = "atlas.dsv.su.se";
	private static final String URL = "jdbc:mysql://" + COMPUTER_NAME + "/" + DB_NAME;

	private Connection dbConnection;
	
	public void init () throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		dbConnection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
	}
	
	public void close () {
		try {
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateWithPost (GuestPost post) {
		try {
			String query = "INSERT INTO guest_post (name, email, website, comment)"
					+ "VALUES('" + post.getName() + "','" + post.getEmail() + 
					"','" + post.getWebsite() + "','" + post.getComment() + "');";
			Statement statement = dbConnection.createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void update (String query) {
		try {
			Statement statement = dbConnection.createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet select (String query) {
		ResultSet result = null;
		try {
			Statement statement = dbConnection.createStatement();
			result = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
