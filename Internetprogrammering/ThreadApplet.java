import java.applet.Applet;
import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * Uppgift 1.4 Äpplen
 * 
 * @author Sebastian Appelberg
 *
 */
public class ThreadApplet extends Applet implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private Thread thread = new Thread(this);
	private JTextArea textArea = new JTextArea();
	
	@Override
	public void init() {
		setLayout(new BorderLayout());
	    JScrollPane scrollPane = new JScrollPane(textArea);
	    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    add(scrollPane, BorderLayout.CENTER);
	    thread.start();
	}
	
	@Override
	public void run() {
		
		try {
			T11 t1 = new T11(textArea);
			t1.start();
			Thread.sleep(5000);
			T22 t2 = new T22(textArea);
			Thread.sleep(5000);
			t1.stopThread();
			Thread.sleep(5000);
			t2.stopThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
