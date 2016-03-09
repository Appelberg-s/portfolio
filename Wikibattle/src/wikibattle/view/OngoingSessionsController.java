package wikibattle.view;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import wikibattle.WikiMainApp;
import wikibattle.model.WikiMatch;

public class OngoingSessionsController implements WikiController {

	@FXML private ListView<WikiMatch> sessionListView;
	
	private WikiMainApp mainApp;
	
	@FXML
	private void initialize () {
		sessionListView.setCellFactory(lv -> {
			return new ListCell<WikiMatch>() {
				@Override
				protected void updateItem (WikiMatch item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(item.toString());
						if (item.getWikiMatchResults() != null)
							return;
						MenuItem acceptItem = new MenuItem("Accept Challenge");
						acceptItem.setOnAction(e -> handleChallenge(item));
						MenuItem denyItem = new MenuItem("Deny Challenge");
						denyItem.setOnAction(e -> handleChallengeDenied(item));
						ContextMenu contextMenu = new ContextMenu(acceptItem, denyItem);
						setContextMenu(contextMenu);
					}
				}
			};
		});
	}
	
	@FXML
	private void handleBackButton () {
		mainApp.SetMainWindowAndWire("StartMenu.fxml");
	}
	
	@FXML
	private void handleRefresh () {
		if (sessionListView.getItems().size() != mainApp.getWikiSession().getMatches().size()) {
			sessionListView.getItems().clear();
			sessionListView.getItems().addAll(mainApp.getWikiSession().getMatches());
		}
	}
	
	private void handleChallenge (WikiMatch match) {
		FXMLLoader loader = new FXMLLoader(OngoingSessionsController.class.getResource("WikiWebView.fxml"));
		try {
			StackPane node = (StackPane) loader.load();
			WikiWebViewController controller = loader.getController();
			match.setIsChallenge(false);
			controller.setMainApp(mainApp);
			controller.setWikiMatch(match);
			Platform.runLater(() -> mainApp.setMainWindow(node));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleChallengeDenied (WikiMatch match) {
		mainApp.getWikiSession().removeChallenge(match);
		handleRefresh();
	}
	
	@Override
	public void setMainApp(WikiMainApp mainApp) {
		this.mainApp = mainApp;
		sessionListView.getItems().addAll(mainApp.getWikiSession().getMatches());
	}

}
