import java.util.Properties;

public class PropsConfigurer {

	public static Properties getSmtpProperties (MailHost mailHost) {
		Properties props = new Properties();
		props = getSmtpProperties(mailHost.serverURL, "587");
		return props;
	}
	
	public static Properties getSmtpProperties (String host, String port) {
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.starttls.enable", true);
		return props;
	}
	
}