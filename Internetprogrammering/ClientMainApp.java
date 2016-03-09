import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Uppgift 2.1.1 Stream sockets på klientsidan
 * 
 * @author Sebastian Appelberg
 *
 */

public class ClientMainApp extends Application {
	
	private Stage primaryStage;
	private ClientConnection connection;
	private static String host = "127.0.0.1";
	private static int port = 2000;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				connection.closeConnection();
				primaryStage.close();
			}
		});
		connection = new ClientConnection(host, port); 
		if (connection != null)
			primaryStage.setTitle(host + " : " + port);
		setMainWindow("MainChatWindow.fxml");
		primaryStage.show();
	}
	
	public synchronized void setMainWindow (String fxmlUrl) {
		try {
			FXMLLoader loader = new FXMLLoader((getClass().getResource(fxmlUrl)));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			MainChatWindowController controller = loader.getController();
			controller.setConnection(connection);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		switch (args.length) {
		case 1:
			host = args[0];
			break;
		case 2:
			host = args[0];
			port = Integer.parseInt(args[1]);
			break;
		}
		launch(args);
	}

}
