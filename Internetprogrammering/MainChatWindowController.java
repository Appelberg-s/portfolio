

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;


public class MainChatWindowController {

	@FXML private TextArea messageArea;
	@FXML private TextArea chatArea;
	@FXML private MenuItem menuItemClearChat;
	@FXML private MenuBar menuBar;
	
	private SimpleStringProperty incommingMessage;
	private ClientConnection connection; 
	
	@FXML
	private void initialize () {
		messageArea.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				e.consume();
				handleSend();
			}
		});
	}
	
	@FXML
	private void handleSend () {
		connection.sendMessage(messageArea.getText());
		messageArea.clear();
	}
	
	@FXML
	private void clearChat () {
		chatArea.clear();
	}
	
	public void setConnection(ClientConnection connection) {
		this.connection = connection;
		incommingMessage = connection.getMessageProperty();
		incommingMessage.addListener((obs, oldValue, newValue) -> {
			Platform.runLater(() -> chatArea.appendText(newValue));
			incommingMessage.set("\n");
		});
	}
	
}
