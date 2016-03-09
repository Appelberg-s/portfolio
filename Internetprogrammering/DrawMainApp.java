import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * Uppgift 2.2.1 Datagramsockets med unicast
 * 
 * @author Sebastian Appelberg
 *
 */
public class DrawMainApp extends Application {
	
	private static int port;
	private static int remotePort;
	private static String host;
	private DatagramSocket sender;
	private DatagramSocket receiver;
	private InetSocketAddress adress;
	private GraphicsContext graphics;
	private GraphicsContext graphicsIn;
	private Canvas canvas;
	private Canvas canvasIn;
	
	private boolean alive = true;

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		adress = new InetSocketAddress(host, remotePort);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				alive = false;
				sender.close();
				receiver.close();
				clearCanvas();
				primaryStage.close();
			}
		});
		
		receiver = new DatagramSocket(new InetSocketAddress("192.168.1.65", port));
		sender = new DatagramSocket();
		
		Rectangle2D screen = Screen.getPrimary().getVisualBounds();
		canvas = new Canvas(screen.getWidth(), screen.getHeight());
		canvasIn = new Canvas(screen.getWidth(), screen.getHeight());
		Group centerGroup = new Group();
		centerGroup.getChildren().add(canvasIn);
		centerGroup.getChildren().add(canvas);
		
		HBox topBox = new HBox(15);
		ColorPicker picker = new ColorPicker();
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(ave -> clearCanvas());

		picker.setOnAction(ave -> graphics.setStroke(picker.getValue()));
		topBox.getChildren().add(picker);
		topBox.getChildren().add(clearButton);
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBox);
		borderPane.setCenter(centerGroup);
		
		Scene scene = new Scene(borderPane, 500, 500);
		
		graphics = canvas.getGraphicsContext2D();
		graphicsIn = canvasIn.getGraphicsContext2D();
		
		graphicsIn.setLineWidth(3);
		graphics.setLineWidth(3);
		
		picker.setValue(Color.BLACK);
		
		canvas.setOnMousePressed(event -> {
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				graphics.clearRect(event.getX(), event.getY(), 10, 10);
			} else {
				sendPoint(event.getX(), event.getY(), 1, adress);
				graphics.beginPath();
				graphics.moveTo(event.getX(), event.getY());
				graphics.lineTo(event.getX(), event.getY());
				graphics.stroke();
			}
		});
		
		canvas.setOnMouseDragged(event -> {
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				graphics.clearRect(event.getX()-5, event.getY()-5, 10, 10);
			} else {
				sendPoint(event.getX(), event.getY(), adress);
				graphics.lineTo(event.getX(), event.getY());
				graphics.stroke();
			}
		});
		
		new Thread(() -> receivePoint()).start();
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void erase (GraphicsContext gc, MouseEvent e, int size) {
		gc.clearRect(e.getX(), e.getY(), size, size);
	}
	
	/**
	 * Denna metod rensar ritytan. Observera att mottagarens rityta inte rensas. 
	 */
	public void clearCanvas () {
		graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		graphics.closePath();
		graphicsIn.clearRect(0, 0, canvasIn.getWidth(), canvasIn.getHeight());
		graphicsIn.closePath();
	}
	
	public void sendPoint (double x, double y, SocketAddress address) {
		byte[] buf = (x + " " + y).getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address);
		try {
			sender.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendPoint (double x, double y, int moveTo, SocketAddress address) {
		byte[] buf = (x + " " + y + " " + moveTo).getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address);
		try {
			sender.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Vad för typ av innehåll i paketet som tas emot avgörs av hur många mellanrum som skickats med,
	 * d.v.s. hur stor arrayen blir när man splittar strängen. 
	 * En sträng med ett mellanrum betyder att det är punkt som ska användas för lineTo.
	 * En sträng med två mellan betyder att det är en punkt som ska användas för MoveTo.
	 */
	public void receivePoint () {
		while (alive) {
			try {
				byte[] buf = new byte[30];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				receiver.receive(packet);
				String[]xy = new String(buf).split(" ");
				if (xy.length > 2) {
					graphicsIn.beginPath();
					graphicsIn.moveTo(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
					graphicsIn.lineTo(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
					graphicsIn.stroke();
				} else {
					graphicsIn.lineTo(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
					graphicsIn.stroke();
				}
			} catch (SocketException e) {
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		port = Integer.parseInt(args[0]);
		host = args[1];
		remotePort = Integer.parseInt(args[2]);
		launch(args);
	}
	
	
}
