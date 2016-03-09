import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


public class MainServerWindowController {
	
	@FXML private ListView<ClientHandler> connectionList;
	@FXML private TextArea broadcastArea;
	
	private SimpleStringProperty messageProperty;
	private Stage primaryStage;
	
	@FXML
	private void initialize () {
		
		connectionList.setCellFactory(lv -> {
			return new ListCell<ClientHandler>() {
				@Override
				protected void updateItem (ClientHandler item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(item.toString());
						MenuItem kickItem = new MenuItem("Kick client");
						kickItem.setOnAction(ae -> kickClient());
						ContextMenu contextMenu = new ContextMenu(kickItem);
						setContextMenu(contextMenu);
					}
				}
			};
		});
		
	}
	
	public void setServer (ChatServer server) {
		connectionList.setItems(server.getClientConnections());
		server.getClientConnections().addListener(new ListChangeListener<ClientHandler>() {
			@Override
			public void onChanged(Change<? extends ClientHandler> c) {
				c.next();
				if (c.wasAdded())
					primaryStage.setTitle("Clients: " +  connectionList.getItems().size() + " " + c.getAddedSubList().get(0).toString());
				else if(c.wasRemoved()) 
					primaryStage.setTitle("Clients: " +  connectionList.getItems().size());
			}
		});
		messageProperty = server.getMessageProperty();
		messageProperty.addListener((obs, oldValue, newValue) -> {
			Platform.runLater(() -> broadcastArea.appendText(newValue));
			messageProperty.set("\n");
		});
	}
	
	public void setStage (Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	private void kickClient () {
		connectionList.getSelectionModel().getSelectedItem().sendMessage("Kicked from server");
		connectionList.getSelectionModel().getSelectedItem().closeConnection();
	}

}
