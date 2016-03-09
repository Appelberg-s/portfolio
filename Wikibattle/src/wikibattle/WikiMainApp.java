package wikibattle;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import wikibattle.model.WikiPlayer;
import wikibattle.model.WikiSession;
import wikibattle.view.ScoreScreenController;
import wikibattle.view.WikiController;
import wikibattle.view.WikiWebViewController;


public class WikiMainApp extends Application {

	private Stage primaryStage;
	private WikiSession wikiSession; 
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("WikiBattle");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (wikiSession != null)
					wikiSession.endSession();
				primaryStage.close();
			}
		});
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../logo2.jpg")));
		SetMainWindowAndWire("LoginScreen.fxml");
		primaryStage.show();
	}
	
	public WikiPlayer getMainPlayer() {
		if (wikiSession == null)
			return null;
		return wikiSession.getPlayer();
	}
	
	public void setWikiSession(WikiSession wikiSession) {
		this.wikiSession = wikiSession;
	}
	
	public WikiSession getWikiSession() {
		return wikiSession;
	}
	
	public void SetMainWindowAndWire (String fxmlURL) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("view/" + fxmlURL));
		try {
			Parent node = loader.load();
			WikiController controller = loader.getController();
			controller.setMainApp(this);
			setMainWindow(node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public StackPane setStackPaneMainWindow (Parent node, String newWindow,
			WikiWebViewController webViewController) {
		StackPane stackPane = new StackPane();
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("view/" + newWindow));
		Parent node2 = null;
		ScoreScreenController controller = null;
		try {
			node2 = loader2.load();
			controller = loader2.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		node.setDisable(true);
		
		DropShadow dropShadow = new DropShadow();
		GaussianBlur blur = new GaussianBlur();
		blur.setRadius(5);
		
		node.setEffect(blur);
		node2.setEffect(dropShadow);
		stackPane.getChildren().addAll(node, node2);
		setMainWindow(stackPane);
		return stackPane;
	}
	
	public void setMainWindow (Parent node) {
		Scene scene = new Scene(node);
		scene.getStylesheets().add(WikiMainApp.class.getResource("MainTheme.css").toExternalForm());
		primaryStage.setScene(scene);
	}
	
	public void setMainWindow (Scene scene) {
		scene.getStylesheets().add(WikiMainApp.class.getResource("MainTheme.css").toExternalForm());
		primaryStage.setScene(scene);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
