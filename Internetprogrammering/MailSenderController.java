import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;

public class MailSenderController {
	
	@FXML private TextField fromField;
	@FXML private TextField toField;
	@FXML private TextField subjectField;
	@FXML private TextField mailServerField; 
	@FXML private TextArea messageArea;
	@FXML private PasswordField passwordField;
	
	private MailSender mailSender;
	private String username;
	private String password;
	
	@FXML
	private void handleSend () {
		if (!passwordField.getText().isEmpty() || !fromField.getText().isEmpty())
			setUserInfo(fromField.getText(), passwordField.getText());
		String from = fromField.getText();
		String to = toField.getText();
		String subject = subjectField.getText();
		String message = messageArea.getText();
		if (to.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("You need to fill in a to-adress");
			alert.showAndWait();
		}
		new Thread(() -> sendMail(from, to, subject, message)).start();
	}
	
	private void sendMail (String from, String to, String subject, String message) {
		if (mailSender == null && mailServerField.getText().isEmpty()) {
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("You need to fill in mail server.");
				alert.showAndWait();
			});
		} else {
			try {
				if (mailSender == null)
					mailSender = new MailSender(PropsConfigurer.getSmtpProperties(mailServerField.getText(), "587"), username, password);
				mailSender.sendMail(from, to, subject, message);
			} catch (AddressException e) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Something went wrong when sending the mail. Check the if the to-adress is correct.");
					alert.showAndWait();
				});
				return;
			} catch (MessagingException e) {
				Platform.runLater(() -> {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setContentText("Something went wrong when sending the mail");
					alert.showAndWait();
				});
				return;
			}
			Platform.runLater(() -> messageArea.setText("Mail sent!"));
		}
			
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
		fromField.setText(mailSender.getUsername());
	}
	
	public void setUserInfo (String username, String password) {
		this.username = username;
		this.password = password;
		fromField.setText(username);
		passwordField.setText(password);
	}

}
