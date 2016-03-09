package wikibattle.view;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import wikibattle.WikiMainApp;
import wikibattle.model.WikiMatch;
import wikibattle.model.WikiPlayer;

public class NewGameController implements WikiController {
	
	private final static String EMPTY_WORD_PROMPT = "A word needs to be filled in";
	private final static String INCORRECT_ARTICLE_PROMPT = "That word doesn't lead to an article";

	@FXML private TextField destinationWordField;
	@FXML private Button readyButton;
	@FXML private ComboBox<WikiPlayer> friendsComboBox;
	
	private WikiMainApp mainApp;
	private WikiMatch wikiMatch;
	
	@FXML
	private void initialize () {
		initTestingValues();
	}
	
	private void initTestingValues() {
		destinationWordField.setText("Blue");
	}
	
	@FXML
	private void handleBackButton () {
		mainApp.SetMainWindowAndWire("StartMenu.fxml");
	}
	
	@FXML 
	private void handleReadyButton () {
		if (friendsComboBox.getSelectionModel().getSelectedItem() == null 
				|| destinationWordField.getText().isEmpty())
			tooltipPrompt(destinationWordField, EMPTY_WORD_PROMPT);
		else if (!articleExists("https://en.m.wikipedia.org/wiki/" + destinationWordField.getText().replaceAll(" ", "_")))
			tooltipPrompt(destinationWordField, INCORRECT_ARTICLE_PROMPT);
		else {
			FXMLLoader loader = new FXMLLoader(NewGameController.class.getResource("WikiWebView.fxml"));
			try {
				StackPane node = (StackPane) loader.load();
				WikiWebViewController controller = loader.getController();
				wikiMatch = new WikiMatch(mainApp.getMainPlayer(), friendsComboBox.getSelectionModel().getSelectedItem());
				wikiMatch.setDestinationWord(destinationWordField.getText());
				wikiMatch.setIsChallenge(true);
				controller.setMainApp(mainApp);
				controller.setWikiMatch(wikiMatch);
				Platform.runLater(() -> mainApp.setMainWindow(node));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean articleExists (String article) {
		try {
			URL url = new URL(article);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void tooltipPrompt (TextField textField, String prompt) {
		Tooltip tooltip = new Tooltip(prompt);
		Point2D point = textField.localToScreen(textField.getLayoutBounds().getMinX(), textField.getLayoutBounds().getMinY());
		
		textField.setOnMouseClicked(event -> tooltip.hide());
		tooltip.setOnShown(event -> {
			Timer timer = new Timer(true);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> tooltip.hide()); 
					timer.cancel();
				}
			}, 3000);
		});
		tooltip.show(textField, point.getX(), point.getY() + 25);
	}
	
	public void setMainApp(WikiMainApp mainApp) {
		this.mainApp = mainApp;
		friendsComboBox.getItems().addAll(mainApp.getWikiSession().getFriends());
		friendsComboBox.getSelectionModel().selectFirst();
	}
	
	public void setFriendSelected (WikiPlayer friend) {
		friendsComboBox.getSelectionModel().select(friend);
	}
	
	public void setWikiMatch (WikiMatch wikiMatch) {
		this.wikiMatch = wikiMatch;
	}

}
