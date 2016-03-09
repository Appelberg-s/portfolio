public enum MailHost {
	GOOGLE("smtp.gmail.com"), HOTMAIL("smtp.live.com"), 
	OUTLOOK("smtp-mail.outlook.com"), YAHOO("smtp.mail.yahoo.com");
	
	protected String serverURL;
	
	MailHost (String serverURL) {
		this.serverURL = serverURL;
	}
	
}