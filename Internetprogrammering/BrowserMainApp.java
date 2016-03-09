import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Uppgift 3.1 Webbserverkopplingar
 * 
 * @author Sebastian Appelberg
 *
 */
public class BrowserMainApp extends Application {

	private BorderPane root;
	private TextField searchField;
	private TextArea browserArea;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
				
		root = new BorderPane();
		searchField = new TextField();
		browserArea = new TextArea();
		browserArea.setEditable(false);
		
		root.setTop(searchField);
		root.setCenter(browserArea);
		
		searchField.setOnAction(ave -> handleSearch());
		
		Scene scene = new Scene(root, 1000, 800);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void handleSearch () {
		Point2D pos = searchField.localToScreen(searchField.getLayoutBounds().getMinX(), searchField.getLayoutBounds().getMinY());
		String text = "";
		Tooltip tooltip = new Tooltip("That website probably doesn't exist");
		tooltip.setOnShown(event -> {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> tooltip.hide());
					timer.cancel();
				}
			}, 2500);
		});
		searchField.setOnMouseMoved(event -> tooltip.hide());
		try {
			InputStream is = new URL(searchField.getText()).openStream();
			Scanner scanner = new Scanner(is);
			text = scanner.useDelimiter("\\A").next();
			scanner.close();
			is.close();
		} catch (FileNotFoundException e) {
			tooltip.show(searchField, pos.getX() + 10, pos.getY() + searchField.getHeight());
		} catch (MalformedURLException e) {
			tooltip.setText("The URL you have entered is malformed");
			tooltip.show(searchField, pos.getX() + 10, pos.getY() + searchField.getHeight());
		} catch (IOException e) {
			tooltip.setText("Something might be wrong with your connection");
			tooltip.show(searchField, pos.getX() + 10, pos.getY() + searchField.getHeight());
		}
		browserArea.setText(text);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
