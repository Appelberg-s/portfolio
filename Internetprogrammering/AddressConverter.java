public class AddressConverter {
	
	public static MailHost convertToMailHost (String address) throws Exception {
		String domain = address.split("@")[1];
		switch (domain) {
		case "gmail.com":
			return MailHost.GOOGLE;
		case "hotmail.com":
			return MailHost.HOTMAIL;
		case "outlook.com":
			return MailHost.OUTLOOK;
		case "yahoo.com":
			return MailHost.YAHOO;
		default:
			throw new Exception("Email provider not supported"); 
		}
	}

}
