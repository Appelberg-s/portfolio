package wikibattle.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WikiMatchResult {
	
	private WikiPlayer winner;
	private final WikiPlayer loser;
	private boolean draw = false;
	private int winnerClicks;
	private int loserClicks;
	private Date date;
	
	public WikiMatchResult (WikiPlayer winner, WikiPlayer loser, int winnerClicks, int loserClicks,  
			Date date) {
		this.winnerClicks = winnerClicks;
		this.loserClicks = loserClicks;
		this.winner = winner;
		this.loser = loser;
		this.date = date;
	}
	
	public void setDraw (int draw) {
		this.draw = draw == 1 ? true : false;
	}
	
	public int isDraw () {
		return draw ? 1 : 0;
	}
	
	public WikiPlayer getWinner() {
		return winner;
	}
	
	public WikiPlayer getLoser() {
		return loser;
	}
	
	public int getWinnerClicks() {
		return winnerClicks;
	}
	
	public int getLoserClicks() {
		return loserClicks;
	}
	
	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
		if (draw)
			return "Draw between " + winner + " and " + loser +  "!" + "\t" + dateFormat.format(date);
		return "Winner: " + winner + ", loser: " + loser + "\t" + dateFormat.format(date);  
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof WikiMatchResult) {
			WikiMatchResult other = (WikiMatchResult) obj;
			return winner.equals(other.winner) && loser.equals(other.loser)
					&& date.equals(other.date);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return date.hashCode();
	}
	
}
