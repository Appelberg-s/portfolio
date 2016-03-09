package wikibattle.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WikiMatch {
	
	private WikiPlayer player;
	private WikiPlayer opponent;
	private String destinationWord;
	private String startWord;
	private int clicks = -1; 
	private WikiMatchResult wikiMatchResults;
	private Date date;
	private boolean isChallenge;
	
	public WikiMatch() {
	}
	
	public WikiMatch(WikiPlayer player, WikiPlayer opponent) {
		this.player = player;
		this.opponent = opponent;
	}
	
	public void setWikiMatchResults(WikiMatchResult wikiMatchResults) {
		this.wikiMatchResults = wikiMatchResults;
	}
	
	public WikiMatchResult getWikiMatchResults() {
		return wikiMatchResults;
	}
	
	public void setPlayer(WikiPlayer player) {
		this.player = player;
	}
	
	public void setOpponent(WikiPlayer opponent) {
		this.opponent = opponent;
	}
	
	public void setClicks(int clicks) {
		this.clicks = clicks;
	}
	
	public void setDestinationWord(String destinationWord) {
		this.destinationWord = destinationWord;
	}
	
	public String getDestinationWord() {
		return destinationWord;
	}
	
	public void setStartWord(String startWord) {
		this.startWord = startWord;
	}
	
	public String getStartWord() {
		return startWord;
	}
	
	public int getClicks() {
		return clicks;
	}
	
	public WikiPlayer getPlayer() {
		return player;
	}
	
	public WikiPlayer getOpponent() {
		return opponent;
	}
	
	public void setDate (Date date) {
		this.date = date;
	}
	
	public Date getDate () {
		return date;
	}
	
	public void setIsChallenge (boolean isChallenge) {
		this.isChallenge = isChallenge;
	}
	
	public boolean isChallenge () {
		return isChallenge;
	}
	
	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
		if (wikiMatchResults != null)
			return wikiMatchResults.toString();
		return "Versus: " + opponent.getName() + ", " + destinationWord + " - " + clicks + " clicks"+ "\t" + dateFormat.format(date); 
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof WikiMatch) {
			WikiMatch other = (WikiMatch) obj;
			if (isChallenge)
				return opponent.equals(other.opponent) && player.equals(other.player)
						&& date.equals(other.date);
			if (wikiMatchResults != null)
				return this.wikiMatchResults.equals(other.wikiMatchResults);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if (!isChallenge && wikiMatchResults != null)
			return wikiMatchResults.hashCode();
		return date.hashCode();
	}

}
