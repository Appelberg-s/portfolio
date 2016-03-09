import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.property.SimpleObjectProperty;

public class ClientDiConnection {
	
	private ServerSocket inConnection;
	private ExecutorService executor = Executors.newCachedThreadPool();
	private SimpleObjectProperty<Storage> messageProperty = new SimpleObjectProperty<>();
	
	public ClientDiConnection(int inPort) throws IOException {
		inConnection = new ServerSocket(inPort);
		receive();
	}
	
	public void send (Storage storage, String host, int port) throws UnknownHostException, ConnectException, IOException {
		Socket connection = new Socket(host, port);
		ObjectOutputStream oos = new ObjectOutputStream(new 
				BufferedOutputStream(connection.getOutputStream()));
		oos.writeObject(storage);
		oos.flush();
		oos.close();
		connection.close();
	}
	
	private void receive () { 
		executor.submit(() -> {
			while (!executor.isShutdown()) {
				try (Socket input = inConnection.accept();
						ObjectInputStream ois = new ObjectInputStream(new 
								BufferedInputStream(input.getInputStream()));){
					Storage storage = (Storage) ois.readObject();
					messageProperty.set(storage);
				} catch(SocketException e) {
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void closeConnection () {
		executor.shutdown();
		try {
			inConnection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SimpleObjectProperty<Storage> getMessageProperty() {
		return messageProperty;
	}
	
}
