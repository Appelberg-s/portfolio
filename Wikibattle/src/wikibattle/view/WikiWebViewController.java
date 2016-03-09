package wikibattle.view;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import wikibattle.WikiMainApp;
import wikibattle.model.WikiMatch;
import wikibattle.model.WikiMatchResult;

public class WikiWebViewController implements WikiController {
	
	private static final String RANDOM_ARTICLE = "https://en.m.wikipedia.org/wiki/special:random/#/random";
	private final String [] elementsToRemove = {"footer", "ca-edit", "ca-watch", "searchInput",
			"mw-mf-main-menu-button", "page-secondary-actions", "mw-mf-last-modified", "header"};
	
	@FXML private WebView webView;
	@FXML private Label linksLabel;
	@FXML private Label destinationLabel;
	@FXML private BorderPane root;
	@FXML private StackPane rootStackPane;
	
	private WebEngine webEngine;
	private WikiMainApp mainApp;
	private String destinationAddress; 
	private String startWord;
	
	private WikiMatch wikiMatch;
	
	@FXML
	private void initialize () {
		webEngine = webView.getEngine();
		intializeListeners();
		loadRandomStartingPoint();
	}
	
	public void setMainApp(WikiMainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setWikiMatch (WikiMatch wikiMatch) {
		this.wikiMatch = wikiMatch;
		this.destinationAddress = "https://en.m.wikipedia.org/wiki/" + wikiMatch.getDestinationWord();
		destinationLabel.setText(wikiMatch.getDestinationWord());
	}
	
	private void handleGameFinished () {
		int clicks = Integer.parseInt(linksLabel.getText());
		if (!wikiMatch.isChallenge()) {
			if (clicks < wikiMatch.getClicks()) {
				WikiMatchResult results = new WikiMatchResult(wikiMatch.getPlayer(), wikiMatch.getOpponent()
						, clicks, wikiMatch.getClicks(), new Date());
				wikiMatch.setWikiMatchResults(results);
			} else if (clicks == wikiMatch.getClicks()) {
				WikiMatchResult results = new WikiMatchResult(wikiMatch.getPlayer(), wikiMatch.getOpponent()
						, clicks, wikiMatch.getClicks(), new Date());
				results.setDraw(1);
				wikiMatch.setWikiMatchResults(results);
			} else {
				WikiMatchResult results = new WikiMatchResult(wikiMatch.getOpponent(), wikiMatch.getPlayer()
						, wikiMatch.getClicks(), clicks, new Date());
				wikiMatch.setWikiMatchResults(results);
			}
			mainApp.getWikiSession().addCompletedChallenge(wikiMatch);
		} else {
			wikiMatch.setStartWord(startWord);
			wikiMatch.setClicks(clicks);
			wikiMatch.setDate(new Date());
			mainApp.getWikiSession().sendChallenge(wikiMatch);
		}
		Platform.runLater(() -> showScoreScreen());
	}
	
	@FXML
	private void handleBackButton ()  {
		mainApp.SetMainWindowAndWire("StartMenu.fxml");
	}
	
	private void intializeListeners () {
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == Worker.State.RUNNING) {
					incrementLabel(linksLabel);
				} else if (newValue == Worker.State.SUCCEEDED) {
					try {
						Document dom = webEngine.getDocument();
						customizeWebView(dom);
						String location = webEngine.getLocation();
						if (location.contains("("))
							location = location.substring(0, location.lastIndexOf("_"));
						if (location.equalsIgnoreCase(destinationAddress))
							handleGameFinished();
					} catch (DOMException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	private void loadRandomStartingPoint () { 
		webEngine.load(RANDOM_ARTICLE);
		String address= webEngine.getLocation();
		startWord = address.substring(address.lastIndexOf('/') + 1, address.length());
	}
	
	private void showScoreScreen () {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoreScreen.fxml"));
			BorderPane node = (BorderPane) loader.load();
			ScoreScreenController controller = loader.getController();
			controller.setMainApp(mainApp); 
			controller.setWebViewController(this);
			controller.setWikiMatch(wikiMatch);
			
			GaussianBlur blur = new GaussianBlur(5);
			DropShadow dropShadow = new DropShadow();
			root.setEffect(blur);
			root.setDisable(true);
			node.setEffect(dropShadow);
			
			rootStackPane.getChildren().add(node);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeScoreScreen () {
		root.setEffect(null);
		root.setDisable(false);
		rootStackPane.getChildren().remove(1);
	}
	
	private void incrementLabel (Label label) {
		Platform.runLater(() -> label.setText("" + (Integer.parseInt(label.getText()) + 1)));
	}
	
	private void customizeWebView (Document dom) {
		Node n2 = dom.getElementById("mw-mf-main-menu-button");
		if (n2 != null) {
			Node toRemove = n2.getParentNode().getParentNode();
			Node parent = n2.getParentNode().getParentNode().getParentNode();
			parent.removeChild(toRemove);
		}
		Arrays.stream(elementsToRemove)
		.map(e -> dom.getElementById(e))
		.filter(e -> e != null)
		.forEach(e -> e.getParentNode().removeChild(e));
	}
	

}
