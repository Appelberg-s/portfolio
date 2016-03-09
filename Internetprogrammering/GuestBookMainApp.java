import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * Uppgift 3.2 Databaskopplingar
 * 
 * @author Sebastian Appelberg
 *
 */
public class GuestBookMainApp extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("GuestBookView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("GuestBook");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
