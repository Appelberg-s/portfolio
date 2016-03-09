package wikibattle.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import wikibattle.WikiMainApp;
import wikibattle.model.WikiMatch;
import wikibattle.model.WikiMatchResult;

public class ScoreScreenController implements WikiController {

	@FXML private Label outcomeLabel;
	@FXML private Label player1Clicks;
	@FXML private Label player2Clicks;
	@FXML private Label player1Name;
	@FXML private Label player2Name;
	@FXML private Label totalClicks2;
	
	private WikiMainApp mainApp;
	private WikiWebViewController webViewController;
	
	public void setWebViewController(WikiWebViewController webViewController) {
		this.webViewController = webViewController;
	}
	
	@Override
	public void setMainApp(WikiMainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	public void setWikiMatch (WikiMatch wikiMatch) {
		if (wikiMatch.isChallenge()) {
			player1Name.setText(wikiMatch.getPlayer().getName());
			player1Clicks.setText("" + wikiMatch.getClicks());
			outcomeLabel.setText("Challenge sent!");
			totalClicks2.setText("");
		} else {
			WikiMatchResult results = wikiMatch.getWikiMatchResults();
			player1Name.setText(results.getWinner().getName());
			player1Clicks.setText("" + results.getWinnerClicks());
			player2Name.setText(results.getLoser().getName());
			player2Clicks.setText("" + results.getLoserClicks());
			if (results.isDraw() == 1)
				outcomeLabel.setText("It's a draw!");
			else
				outcomeLabel.setText(results.getWinner() + " Wins!");
			mainApp.getWikiSession().removeChallenge(wikiMatch);
		}
	}
	
	@FXML
	private void handleBackButton () {
		mainApp.SetMainWindowAndWire("StartMenu.fxml"); 
	}
	
	@FXML
	private void handleContinueButton () {
		webViewController.removeScoreScreen();
	}

}
