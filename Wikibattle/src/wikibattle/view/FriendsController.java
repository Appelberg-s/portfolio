package wikibattle.view;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import wikibattle.WikiMainApp;
import wikibattle.model.WikiPlayer;
import wikibattle.util.DatabaseConnection;

public class FriendsController implements WikiController {
	
	@FXML private TextField friendField;
	@FXML private ListView<WikiPlayer> friendList;
	
	private WikiMainApp mainApp;
	private DatabaseConnection database;
	
	@FXML
	private void initialize () {
		friendList.setCellFactory(lv -> {
			return new ListCell<WikiPlayer>() {
				@Override
				protected void updateItem (WikiPlayer item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(item.toString());
						MenuItem challengeItem = new MenuItem("Challenge");
						challengeItem.setOnAction(e -> handleChallenge(item));
						ContextMenu contextMenu = new ContextMenu(challengeItem);
						setContextMenu(contextMenu);
					}
				}
			};
		});
	}
	
	@FXML
	private void handleAddFriend () {
		if (friendField.getText().contains(">") || friendField.getText().contains("<")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("");
			alert.setTitle("");
			alert.setContentText("< and > are illegal characters!");
			alert.showAndWait();
		} else if (friendList.getItems().contains(new WikiPlayer(friendField.getText()))) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("");
			alert.setTitle("");
			alert.setContentText("You are already friends.");
			alert.showAndWait();
		} else {
			String select = "SELECT * FROM user WHERE username = '" + friendField.getText() + "';";
			ResultSet result = database.select(select);
			try {
				if (!result.next()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("");
					alert.setTitle("");
					alert.setContentText("Sorry, no user with that name exists.");
					alert.showAndWait();
				} else {
					mainApp.getWikiSession().addFriend(new WikiPlayer(friendField.getText()));
					friendList.getItems().add(new WikiPlayer(friendField.getText()));
				}
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("");
				alert.setTitle("");
				alert.setContentText("Something went wrong with the database connection.");
				alert.showAndWait();
			}
		}
	}
	
	@FXML
	private void handleBackButton () {
		mainApp.SetMainWindowAndWire("StartMenu.fxml");
	}

	@Override
	public void setMainApp(WikiMainApp mainApp) {
		this.mainApp = mainApp;
		database = mainApp.getWikiSession().getDatabase();
		friendList.getItems().addAll(mainApp.getWikiSession().getFriends());
	}
	
	private void handleChallenge (WikiPlayer friend) {
		FXMLLoader loader = new FXMLLoader(FriendsController.class.getResource("NewGame.fxml"));
		try {
			AnchorPane node = (AnchorPane) loader.load();
			NewGameController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setFriendSelected(friend);
			Platform.runLater(() -> mainApp.setMainWindow(node));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
