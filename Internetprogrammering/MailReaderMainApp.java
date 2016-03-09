import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Uppgift 4.2 Epost-mottagning
 * 
 * @author Sebastian Appelberg
 *
 */
public class MailReaderMainApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Mail Application");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MailReaderMainView.fxml"));
		Parent loginView = loader.load();
		primaryStage.setScene(new Scene(loginView));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
