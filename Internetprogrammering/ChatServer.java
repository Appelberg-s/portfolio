import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ChatServer {
	
	private ServerSocket server;
	private int port;
	
	private ExecutorService executor = Executors.newFixedThreadPool(50);
	private ObservableList<ClientHandler> clientConnections = FXCollections.observableArrayList();
	private SimpleStringProperty messageProperty;
	
	public ChatServer () {
		port = 2000;
		messageProperty = new SimpleStringProperty();
		new Thread(() -> initServerLoop()).start();
	}
	
	public ChatServer (int port) {
		this.port = port;
		messageProperty = new SimpleStringProperty();
		new Thread(() -> initServerLoop()).start();
	}
	
	public ObservableList<ClientHandler> getClientConnections() {
		return clientConnections;
	}
	
	private void initServerLoop () {
		try {
			server = new ServerSocket(port);
			while (true) {
				Socket connection = server.accept();
				ClientHandler clientHandler = new ClientHandler(connection, this);
				executor.submit(clientHandler);
				Platform.runLater(() -> clientConnections.add(clientHandler));
			}
		} catch (SocketException e) { 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void broadCast (String message) {
		messageProperty.set(message);
		for (ClientHandler clientHandler : clientConnections)
			clientHandler.sendMessage(message);
	}
	
	public synchronized void removeClientConnection (ClientHandler client) {
		Platform.runLater(() -> clientConnections.remove(client));
	}
	
	public SimpleStringProperty getMessageProperty() {
		return messageProperty;
	}
	
	public synchronized void closeConnection () {
		broadCast("Server went offline");
		try {
			for (ClientHandler clientHandler : clientConnections)
				clientHandler.closeConnection();
			executor.shutdownNow();
			if (server != null)
				server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
