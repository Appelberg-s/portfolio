package wikibattle.view;

import javafx.fxml.FXML;
import wikibattle.WikiMainApp;

public class StartMenuController implements WikiController {
	
	private WikiMainApp mainApp;
	
	@FXML
	private void handleContinueButton () {
		mainApp.SetMainWindowAndWire("OngoingSessions.fxml");
	}
	
	@FXML
	private void handleNewGame () {
		mainApp.SetMainWindowAndWire("NewGame.fxml");
	}
	
	@FXML
	private void handleFriendsButton () {
		mainApp.SetMainWindowAndWire("FriendsMenu.fxml"); 
	}
	
	@FXML
	private void handleBackButton () {
		mainApp.SetMainWindowAndWire("LoginScreen.fxml");
	}
	
	public void setMainApp (WikiMainApp mainApp) {
		this.mainApp = mainApp;
	}
	
}
