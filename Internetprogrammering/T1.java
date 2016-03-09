/**
 * Denna klass utnyttjas i ThreadTester och används för att utforska skillnaden mellan
 * att ärva från Thread och implementera gränssnittet Runnable. 
 * 
 * Den frivilliga utökning att kunna pausa tråden har implementerats.
 * 
 * @author Sebastian Appelberg
 *
 */
public class T1 extends Thread implements Pausable {
	
	private boolean alive = true;
	private boolean active = true;
	
	@Override
	public void run() {
		while (alive) {
			while (active) {
				System.out.println("T1: Tråd 1");
				try { sleep(1000); } catch (InterruptedException e) {e.printStackTrace();}
			}
			try { sleep(25); } catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	
	/**
	 * Denna metod stannar tråden.
	 */
	public void stopThread () {
		active = false;
		alive = false;
	}
	
	/**
	 * Denna metod används för att pausa eller återuppta tråden.
	 * @param active - Om parametern är falsk pausas tråden och vice versa. 
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
