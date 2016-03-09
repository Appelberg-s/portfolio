/**
 * Denna tillhör ThreadTester. Läs dokumentation i T1 för mer detaljer. 
 * 
 * @author Sebastian Appelberg
 *
 */
public class T2 implements Runnable, Pausable {

	private Thread thread = new Thread(this);
	private boolean alive = true;
	private boolean active = true;
	
	public T2() {
		thread.start();
	}
	
	@Override
	public void run() {
		while (alive) {
			while (active) {
				System.out.println("T2: Tråd 2");
				try { Thread.sleep(1000); } catch (InterruptedException e) {e.printStackTrace();}
			}
			try { Thread.sleep(25); } catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public void stopThread () {
		active = false;
		alive = false;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

}
