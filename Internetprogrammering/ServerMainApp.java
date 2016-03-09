import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Uppgift 2.1.2 Stream sockets på serversidan
 * 
 * @author Sebastian Appelberg
 *
 */

public class ServerMainApp extends Application {

	private Stage primaryStage;
	private ChatServer server;
	private static int port = 2000;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				server.closeConnection();
				primaryStage.close();
			}
		});
		new Thread(() -> server = new ChatServer(port)).start();
		primaryStage.setTitle("Clients: 0");
		setMainWindow("MainServerWindow.fxml");
		primaryStage.show();
	}
	
	public void setMainWindow (String fxmlUrl) {
		try {
			FXMLLoader loader = new FXMLLoader((getClass().getResource(fxmlUrl)));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			MainServerWindowController controller = loader.getController();
			controller.setServer(server);
			controller.setStage(primaryStage);
			primaryStage.setScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		launch(args);
	}
	
}


