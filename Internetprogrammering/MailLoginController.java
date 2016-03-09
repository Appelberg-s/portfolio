import java.io.IOException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MailLoginController {
	
	@FXML private TextField emailField;
	@FXML private PasswordField passwordField;  
	
	private String email;
	private String password;
	private MailSender mailSender;
	private Stage primaryStage;
	
	@FXML
	private void handleLogin () {
		email = emailField.getText();
		password = passwordField.getText();
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("");
			alert.setContentText("Please put in a valid emailadress");
			alert.showAndWait();
			emailField.requestFocus();
			return;
		}
		try {
			MailHost host = AddressConverter.convertToMailHost(email);
			mailSender = new MailSender(PropsConfigurer.getSmtpProperties(host), email, password);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("");
			alert.setContentText("The mail server you are trying to connect to isn't supported. "
					+ "But if you have the mail server information you can enter it manually.");
			alert.showAndWait();
		}
		continueToMail();
	}
	
	@FXML
	private void handleCancel () {
		primaryStage.close();
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	private void continueToMail () {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MailSenderMainView.fxml"));
			Parent mailView = loader.load();
			MailSenderController senderController = loader.getController();
			if (mailSender == null)
				System.out.println("JH");
			if (mailSender != null)
				senderController.setMailSender(mailSender);
			else
				senderController.setUserInfo(email, password);
			Platform.runLater(() -> primaryStage.setScene(new Scene(mailView)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
