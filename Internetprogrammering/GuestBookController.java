import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class GuestBookController {
	
	private static final String LOADING_ERROR_MESSAGE = "Error while loading data from database. Please check your connection "
			+ "and restart the program.";
	private static final String INPUT_ERROR_MESSAGE = "Text starting with < and ending with > is not allowed.";
	
	private GuestBookDatabase database;
	
	@FXML private TextArea textArea;
	@FXML private TextField nameField;
	@FXML private TextField emailField;
	@FXML private TextField websiteField;
	@FXML private TextArea commentArea;
	
	@FXML
	private void initialize () {
		database = new GuestBookDatabase();
		try {
			database.init();
			updateTextArea();
		} catch (SQLException e) {
			textArea.setText(LOADING_ERROR_MESSAGE);
			nameField.getParent().setDisable(true);
		}
	}
	
	@FXML
	private void handlePost () {
		String name = nameField.getText();
		String email = emailField.getText();
		String website = websiteField.getText();
		String comment = commentArea.getText();
		
		if (name.isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Name field must be filled in!");
			alert.setTitle("");
			alert.showAndWait();
		} else if (name.matches("<.*>")|| email.matches("<.*>") || website.matches("<.*>") 
				|| comment.matches("<.*>")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText(INPUT_ERROR_MESSAGE);
			alert.showAndWait();
		} else {
			database.updateWithPost(new GuestPost(name, email, website, comment));
			
			nameField.clear();
			emailField.clear();
			websiteField.clear();
			commentArea.clear();
			
			updateTextArea();
		}
	}
	
	private void updateTextArea () {
		textArea.setText("Updating posts");
		ResultSet result = database.select("SELECT * FROM guest_post");
		try {
			textArea.clear();
			while (result.next()) {
				textArea.appendText(result.getString(1)
					+ "\nNamn: " + result.getString(2) 
					+ "\nEmail: " + result.getString(3) 
					+ "\nHemsida: " + result.getString(4) 
					+ "\nKommentar: " + result.getString(5) 
					+ "\n\n");
			}
			Platform.runLater(() -> textArea.getParent().requestFocus());
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(LOADING_ERROR_MESSAGE);
			alert.showAndWait();
		}
	}

}
