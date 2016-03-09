import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Uppgift 5.1 Bildöverföring
 * 
 * @author Sebastian Appelberg
 *
 */
public class ImageClientMainApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/ImageClientView.fxml"));
		Parent root = loader.load();
		ImageClientController controller = loader.getController();
		primaryStage.setOnCloseRequest(event -> {
			controller.getConnection().closeConnection();
		});
		Scene scene = new Scene(root);
		primaryStage.setTitle("Image Sender");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
