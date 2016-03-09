import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailSender {
	
	private String mailServer;
	private String username;
	private String password;
	private Session mailSession;
	
	public MailSender (Properties props, String username, String password) {
		this.username = username;
		this.password = password;
		mailServer = props.getProperty("mail.smtp.host");
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		mailSession = Session.getInstance(props, authenticator);
	}
	
	public void setupMailSession (Properties props, String username, String password) {
		mailSession = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	public void sendMail (String from, String recipient, 
			String subject, String text) throws AddressException, MessagingException {
		if (mailSession == null)
			throw new MessagingException("The mail session has not been initialized");
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
		message.setSubject(subject);
		message.setText(text);
		Transport.send(message);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void validatePassword () throws MessagingException {
		try {
			Store store;
			store = mailSession.getStore("imaps");
			store.connect(mailServer, username, password);
			store.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} 
	}
	
}
