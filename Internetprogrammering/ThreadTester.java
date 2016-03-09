/**
 * ThreadTester används för att testa de två angivna sätten att använda trådning i Java. 
 * Ena är genom att ärva från Thread och andra är att implementera gränssnittet Runnable. 
 * 
 * @author Sebastian Appelberg
 *
 */
public class ThreadTester {
	
	public static void main(String[] args) {
		
		try {
			T1 t1 = new T1();
			t1.start();
			Thread.sleep(5000);
			T2 t2 = new T2();
			Thread.sleep(5000);
			t2.setActive(false);
			Thread.sleep(5000);
			t2.setActive(true);
			Thread.sleep(5000);
			t1.stopThread();
			Thread.sleep(5000);
			t2.stopThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
