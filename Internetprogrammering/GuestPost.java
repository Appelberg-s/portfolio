public class GuestPost {
	
	private String name;
	private String email;
	private String website;
	private String comment;
	
	public GuestPost(String name, String email, String website, String comment) {
		this.name = name;
		this.email = email;
		this.website = website;
		this.comment = comment;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getName() {
		return name;
	}
	
	public String getWebsite() {
		return website;
	}
	
	public String getComment() {
		return comment;
	}
	
	
}
