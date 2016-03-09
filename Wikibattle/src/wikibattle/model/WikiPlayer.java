package wikibattle.model;

public class WikiPlayer {
	
	private String name;
	
	public WikiPlayer () {
	}
	
	public WikiPlayer (String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof WikiPlayer)
			return this.name.equals(((WikiPlayer) obj).name);
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
}
