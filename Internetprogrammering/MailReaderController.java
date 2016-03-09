import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class MailReaderController {
	
	@FXML private TextField serverField;
	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private TextArea mailArea;
	
	@FXML
	private void receive () {
		mailArea.setText("");
		if (serverField.getText().isEmpty() || usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("All fields needs to be filled in");
			alert.showAndWait();
		} else {
			mailArea.setText("Retrieving...");
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.submit(() -> getMailContent(serverField.getText(), usernameField.getText(), passwordField.getText()));
			executor.shutdown();
		}
	}
	
	private void getMailContent (String host, String user, String password) {
		String content = "";
		Message[] message;
		try {
			Properties props = new Properties();
			props.put("mail.store.protocol", "imaps");
			Session session = Session.getInstance(props);
			Store store = session.getStore();
			store.connect(host, user, password);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_ONLY);
			message = folder.getMessages();
			int quantity = 0;
			if (message.length > 15) 
				quantity = 15;
			for (int i = message.length-1; i >= message.length-1-quantity; i--) {
				content += "FROM: " + message[i].getFrom()[0] + "\n" + "SUBJECT: " + message[i].getSubject() + "\n";
				if (message[i].isMimeType("text/plain"))
					content += ((String) message[i].getContent() + "\n");
				else
					content += "Mail contains no plain text\n\n";
			}
			folder.close(false);
			store.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
			
		final String finalContent = content;
		Platform.runLater(() -> {
			mailArea.setText(finalContent);
		});
	}
	
}
