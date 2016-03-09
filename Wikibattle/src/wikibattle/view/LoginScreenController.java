package wikibattle.view;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import wikibattle.WikiMainApp;
import wikibattle.model.WikiPlayer;
import wikibattle.model.WikiSession;
import wikibattle.util.DatabaseConnection;

public class LoginScreenController implements WikiController {

	@FXML private TextField userNameField;
	@FXML private PasswordField passwordField;
	@FXML private Button signUpButton;
	@FXML private Button loginButton;
	
	private WikiMainApp mainApp;
	private DatabaseConnection database;
	
	@FXML
	private void initialize () {
		try {
			database = new DatabaseConnection("usr_14615787", "db_14615787", "615787", "atlas.dsv.su.se"); 
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Something went wrong with the database connection.");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void handleSignUp () {
		if (userNameField.getText().contains(">") || userNameField.getText().contains("<")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("< and > are illegal characters!");
			alert.showAndWait();
		} else if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("Username or password can't be empty.");
			alert.showAndWait();
		} else {
			String select = "SELECT * FROM user WHERE username = '" + userNameField.getText() + "';";
			ResultSet result = database.select(select);
			try {
				if (result == null || !result.next()) {
					String query = "INSERT INTO user (username, password) VALUES('" + userNameField.getText() +
							"','" + passwordField.getText() + "')";
					database.update(query);
					mainApp.setWikiSession(new WikiSession(new WikiPlayer(userNameField.getText()), database));
					mainApp.SetMainWindowAndWire("StartMenu.fxml");
				} else {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText("That username already exists!");
					alert.showAndWait();
				}
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Something went wrong with the database connection.");
				alert.showAndWait();
			}
		}
		
	}
	
	@FXML
	private void handleLogin () {
		if (userNameField.getText().contains(">") || userNameField.getText().contains("<")) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setContentText("< and > are illegal characters!");
			alert.showAndWait();
		} else {
			String select = "SELECT * FROM user WHERE username = '" + userNameField.getText() + "';";
			ResultSet result = database.select(select);
			try {
				if (!result.next()) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setContentText("No user with that name exists. Try the sign up button instead!");
					alert.showAndWait();
				} else {
					if (!result.getString(2).equals(passwordField.getText())) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setContentText("Wrong password!");
						alert.showAndWait();
					} else {
						mainApp.setWikiSession(new WikiSession(new WikiPlayer(userNameField.getText()), database));
						mainApp.SetMainWindowAndWire("StartMenu.fxml");
					}
				}
			} catch (SQLException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Something went wrong with the database connection.");
				alert.showAndWait();
			}
		}
	}
	
	@Override
	public void setMainApp(WikiMainApp mainApp) {
		this.mainApp = mainApp;
	}
	
}
