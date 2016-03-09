import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;


public class ClientHandler implements Runnable {
	
	private Socket connection;
	private ChatServer chatServer;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public ClientHandler (Socket connection, ChatServer chatServer) {
		this.connection = connection;
		this.chatServer = chatServer;
		setupStreams();
	}
	
	private void setupStreams () {
		try {
			writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), "ISO-8859-1"), true);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage (String msg) {
		writer.println(msg);
	}
	
	public void readMessage () {
		String msg;
		try {
			while ((msg = reader.readLine()) != null)
				chatServer.broadCast(msg);
			closeConnection();
		} catch (SocketException e)  {
		} catch (IOException e) {
			e.printStackTrace();
		}
		chatServer.broadCast("Client: " + connection.getInetAddress().getHostName() + " Disconnected!");
		chatServer.removeClientConnection(this);
	}
	
	public void closeConnection () {
		try {
			writer.close();
			reader.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getClientSocket () {
		return connection;
	}
	
	@Override
	public String toString() {
		return connection.getInetAddress().getHostName() + " : " + connection.getPort();
	}
	
	@Override
	public void run () {
		chatServer.broadCast("Client: " + connection.getInetAddress().getHostName() + " Connected!");
		new Thread(() -> readMessage()).start();
	}
	
	
}
