import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class T11 extends Thread {
	
	private boolean alive = true;
	private JTextArea textArea;
	
	public T11 (JTextArea textArea) {
		this.textArea = textArea;
	}
	
	@Override
	public void run() {
		while (alive) {
				SwingUtilities.invokeLater(() -> {
					textArea.append("T1: Tråd 1\n");
					textArea.setCaretPosition(textArea.getText().length());
				});
			try { sleep(1000); } catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void stopThread () {
		alive = false;
	}
	
}