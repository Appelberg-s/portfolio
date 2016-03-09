import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.beans.property.SimpleStringProperty;


public class ClientConnection {
	
	private static final int DEFAULT_PORT = 2000;
	
	private Socket connection;
	private PrintWriter output;
	private BufferedReader input;
	private int port;
	private String host;
	private SimpleStringProperty messageProperty;
	
	public ClientConnection () {
		host = "127.0.0.1";
		port = DEFAULT_PORT;
		openConnection();
		System.out.println("T: " + Thread.currentThread().getName());
	}
	
	public ClientConnection (String host) {
		this.host = host;
		port = DEFAULT_PORT;
		openConnection();
	}
	
	public ClientConnection (String host, int port) {
		this.host = host;
		this.port = port;
		openConnection();
	}
	
	public ClientConnection (Socket connection) {
		this.connection = connection;
		openConnection();
	}
	
	public Socket getConnection() {
		return connection;
	}
	
	public void openConnection () {
		try {
			messageProperty = new SimpleStringProperty();
			if (connection == null)
				connection = new Socket(host, port);
		} catch (ConnectException e) {
			System.err.println("No server available");
			return;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setupStreams();
		new Thread(() -> readMessages()).start();
	}
	
	public SimpleStringProperty getMessageProperty() {
		return messageProperty;
	}
	
	public void sendMessage (String message) {
		output.println(message);
	}
	
	public void closeConnection () {
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) { 
		}
	}
	
	private void setupStreams () {
		try {
			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "ISO-8859-1"), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readMessages () {
		try {
			String msg;
			while ((msg = input.readLine()) != null)
				messageProperty.set(msg);
		} catch (SocketException e) {
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	
}
