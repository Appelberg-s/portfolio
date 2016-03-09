import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ImageClientController { 
	
	@FXML private FlowPane pictureFlow;
	@FXML private ImageView sendImageView;
	@FXML private TextField hostField;
	@FXML private TextField portField;
	@FXML private Pane imageViewWrapper;
	
	private ClientDiConnection connection;
	private SimpleObjectProperty<Storage> messageProperty;
	private Tooltip tooltip = new Tooltip();
	
	@FXML 
	private void initialize () {
		tooltip.setOnShown(event -> {
			Timer timer = new Timer(true);
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Platform.runLater(() -> tooltip.hide()); 
					timer.cancel();
				}
			}, 2300);
		});
		
		boolean done = false;
		int port = 2000;
		while (!done) {
			try {
				connection = new ClientDiConnection(port);
				done = true;
			} catch (IOException e) {
				port++;
			}
		}
		messageProperty = connection.getMessageProperty();
		messageProperty.addListener((obs, oldValue, newValue) -> {
			ImageView imgView = new ImageView(new Image(new ByteArrayInputStream(newValue.getData())));
			imgView.setFitHeight(100);
			imgView.setPreserveRatio(true);
			imgView.setCache(true);
			imgView.setSmooth(true);
			
			MenuItem saveItem = new MenuItem("Save image");
			saveItem.setOnAction(event -> {
				saveImage(imgView.getImage());
			});
			ContextMenu contextMenu = new ContextMenu(saveItem);
			
			imgView.setOnContextMenuRequested(event -> {
				contextMenu.show(imgView, event.getScreenX(), event.getScreenY());
			});
			imgView.setOnMouseEntered(event -> {
				imgView.setEffect(new DropShadow());
				if (imgView.getFitHeight() != imgView.getImage().getHeight())
					imgView.setFitHeight(102);
			});
			imgView.setOnMouseExited(event -> {
				imgView.setEffect(null);
				if (imgView.getFitHeight() != imgView.getImage().getHeight())
					imgView.setFitHeight(100);
			});
			imgView.setOnMouseClicked(event -> {
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (imgView.getFitHeight() <= 102)
						imgView.setFitHeight(imgView.getImage().getHeight());
					else
						imgView.setFitHeight(100);
				}
			});
			Platform.runLater(() -> pictureFlow.getChildren().add(imgView));
		});
		
		sendImageView.fitWidthProperty().bind(imageViewWrapper.widthProperty());
		sendImageView.fitHeightProperty().bind(imageViewWrapper.heightProperty());
		sendImageView.setOnDragOver(event -> {
			event.acceptTransferModes(TransferMode.COPY);
		});
		sendImageView.setOnDragDropped(event -> {
			final Dragboard db = event.getDragboard();
			final File file = db.getFiles().get(0);
			try {
				Image img = new Image(new FileInputStream(file));
				Platform.runLater(() -> sendImageView.setImage(img));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			db.clear();
		});
		sendImageView.setOnMouseClicked(event -> {
			loadImage();
		});
		pictureFlow.setStyle("-fx-background-color: white;");
		imageViewWrapper.setStyle("-fx-border-style: segments(5);");
	}
	
	private void saveImage (Image image) {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/pictures"));
		chooser.getExtensionFilters().add(new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
		File file = chooser.showSaveDialog((Stage) pictureFlow.getScene().getWindow());
		if (file != null) {
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ClientDiConnection getConnection() {
		return connection;
	}

	@FXML 
	private void loadImage() {
		FileChooser chooser = new FileChooser();
		chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/pictures"));
		chooser.getExtensionFilters().add(new ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
		chooser.setTitle("Choose an image to send!");
		File file = chooser.showOpenDialog((Stage) pictureFlow.getScene().getWindow());
		if (file != null) {
			try {
				byte[] content = Files.readAllBytes(file.toPath());
				Image img = new Image(new ByteArrayInputStream(content));
				sendImageView.setImage(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML 
	private void handleSend () {
		if (sendImageView.getImage() == null) {
			Point2D pos = sendImageView.localToScreen(sendImageView.getLayoutBounds().getMinX(), sendImageView.getLayoutBounds().getMinY());
			tooltip.setText("Select a picture to send");
			tooltip.show(sendImageView, pos.getX(), pos.getY());
		} else {
			try {
				connection.send(new Storage(imageToBytes(sendImageView.getImage()), "img"), 
						hostField.getText(), Integer.parseInt(portField.getText()));
				sendImageView.setImage(null);
			} catch (NumberFormatException e) {
				Point2D pos = portField.localToScreen(portField.getLayoutBounds().getMinX(), portField.getLayoutBounds().getMinY());
				tooltip.setText("Type a number ranging from 0-65550");
				tooltip.show(portField, pos.getX()+10, pos.getY()+20);
			} catch (UnknownHostException e) {
				Point2D pos = hostField.localToScreen(hostField.getLayoutBounds().getMinX(), hostField.getLayoutBounds().getMinY());
				tooltip.setText("Unkown host");
				tooltip.show(hostField, pos.getX()+10, pos.getY()+20);
			} catch (ConnectException e) {
				Point2D pos = portField.localToScreen(portField.getLayoutBounds().getMinX(), portField.getLayoutBounds().getMinY());
				tooltip.setText("Can't connect to " + hostField.getText() + " at port " + portField.getText());
				tooltip.show(portField, pos.getX()+10, pos.getY()+20);
			} catch (IOException e) {
				Point2D pos = pictureFlow.localToScreen(pictureFlow.getLayoutBounds().getMinX(), pictureFlow.getLayoutBounds().getMinY());
				tooltip.setText("Something went wrong");
				tooltip.show(pictureFlow, pos.getX()+10, pos.getY()+10);
			}
		}
	}
	
	private byte[] imageToBytes (Image image) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteOutput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return byteOutput.toByteArray();
	}
	
}
