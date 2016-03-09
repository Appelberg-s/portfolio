package wikibattle.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import wikibattle.util.DatabaseConnection;

public class WikiSession {
	
	private WikiPlayer player;
	private DatabaseConnection connection;
	private ArrayList<WikiMatch> matches = new ArrayList<>(); 
	private HashSet<WikiPlayer> friends = new HashSet<>();
	
	public WikiSession() {
	}
	
	public WikiSession (WikiPlayer player, DatabaseConnection connection) {
		this.player = player;
		this.connection = connection;
	}
	
	public void sendChallenge (WikiMatch wikiMatch) {
		Timestamp date = new Timestamp(wikiMatch.getDate().getTime());
		String query = "INSERT INTO challenges (challenger, challenged, date, startword, finishword, clicks) VALUES('"
				+ wikiMatch.getPlayer() + "','" + wikiMatch.getOpponent() + "','" + date
				+ "','" + wikiMatch.getStartWord()+ "','" 
				+ wikiMatch.getDestinationWord() + "','" + wikiMatch.getClicks() + "');";
		connection.update(query);
	}
	
	public void removeChallenge (WikiMatch wikiMatch) {
		wikiMatch.setIsChallenge(true);
		Timestamp date = new Timestamp(wikiMatch.getDate().getTime());
		String query = "DELETE FROM challenges WHERE "
				+ "challenger = '" + wikiMatch.getOpponent() 
				+ "' AND challenged = '" + player 
				+ "' AND date = '" + date + "';";
		connection.update(query);
		matches.remove(wikiMatch);
	}
	
	public void addFriend (WikiPlayer friend) {
		String query = "INSERT INTO friendship(user, friend) VALUES('" + player + "','" + friend + "');";
		connection.update(query);
	}
	
	public void addCompletedChallenge (WikiMatch match) {
		WikiMatchResult result = match.getWikiMatchResults();
		Timestamp date = new Timestamp(result.getDate().getTime());
		String query = "INSERT INTO completed_challenges(winner, loser, winnerclicks, loserclicks, draw, date) VALUES('" 
				+ result.getWinner() + "','" + result.getLoser() + "','" + result.getWinnerClicks() 
				+ "','" + result.getLoserClicks() + "','" + result.isDraw() + "','" + date + "');";
		connection.update(query);
	}
	
	public ArrayList<WikiMatch> getMatches() {
		String query = "SELECT * FROM challenges WHERE challenged = '" + player.getName() + "';";
		ResultSet result = connection.select(query);
		String query2 = "SELECT * FROM completed_challenges WHERE winner = '" + player.getName() +
				"' OR loser = '" + player.getName() +"';";
		ResultSet result2 = connection.select(query2);
		try {
			while (result.next()) {
				WikiMatch match = new WikiMatch(player, new WikiPlayer(result.getString(1)));
				match.setDate(new Date(result.getTimestamp((3)).getTime()));
				match.setStartWord(result.getString(4));
				match.setDestinationWord(result.getString(5));
				match.setClicks(result.getInt(6));
				match.setIsChallenge(true);
				if (!matches.contains(match))
					matches.add(match);
			}
			while (result2.next()) {
				WikiMatchResult matchResult = new WikiMatchResult(new WikiPlayer(result2.getString(1)), 
						new WikiPlayer(result2.getString(2)), result2.getInt(3), result2.getInt(4), 
						new Date(result2.getTimestamp(6).getTime()));
				matchResult.setDraw(result2.getInt(5));
				WikiPlayer opponent = result2.getString(1) != player.getName() 
						? new WikiPlayer(result2.getString(1)) : new WikiPlayer(result2.getString(2));
				WikiMatch completeMatch = new WikiMatch(player, opponent);
				completeMatch.setWikiMatchResults(matchResult);
				completeMatch.setIsChallenge(false);
				if (!matches.contains(completeMatch))
					matches.add(completeMatch);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("An error occured while loading matches from database.");
			alert.showAndWait();
		}
		return matches;
	}
	
	public Set<WikiPlayer> getFriends() {
		String query = "SELECT friend FROM friendship WHERE user = '" + player.getName() + "' ORDER BY friend;";
		ResultSet result = connection.select(query);
		try {
			while (result.next()) 
				friends.add(new WikiPlayer(result.getString(1)));
		} catch (SQLException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("An error occured while loading friends from database.");
			alert.showAndWait();
		}
		return friends;
	}
	
	public WikiPlayer getPlayer() {
		return player;
	}
	
	public DatabaseConnection getDatabase () {
		return connection;
	}
	
	public void endSession () {
		if (connection != null)
			connection.close();
	}
	
}
