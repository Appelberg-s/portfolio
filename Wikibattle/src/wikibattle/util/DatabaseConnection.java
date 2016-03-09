package wikibattle.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

	private Connection dbConnection;
	
	public DatabaseConnection (String username, String dbName, String password, String computerName) 
			throws SQLException {
		String url = "jdbc:mysql://" + computerName + "/" + dbName;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		dbConnection = DriverManager.getConnection(url, username, password);
	}
	
	public void close () {
		try {
			dbConnection.close();
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
	
	public boolean exists (String query) {
		ResultSet result = select(query);
		try {
			if (result.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
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
