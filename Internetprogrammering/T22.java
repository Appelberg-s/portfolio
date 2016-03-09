import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class T22 implements Runnable {

	private Thread thread = new Thread(this);
	private boolean alive = true;
	private JTextArea textArea;
	
	public T22 (JTextArea textArea) {
		this.textArea = textArea;
		thread.start();
	}
	
	@Override
	public void run() {
		while (alive) {
			SwingUtilities.invokeLater(() -> {
				textArea.append("T2: Tråd 2\n");
				textArea.setCaretPosition(textArea.getText().length());
			});
			try { Thread.sleep(1000); } catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void stopThread () {
		alive = false;
	}
	
}
