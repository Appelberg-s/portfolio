import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Uppgift 6.5.1 Portscanning
 * 
 * @author Sebastian Appelberg
 *
 */
public class PortScan {
	
	private String hostStart;
	private String hostEnd;
	private int portStart;
	private int portEnd;
	private String fileName;
	private HashMap<String, ArrayList<Integer>> openPorts = new HashMap<>();
	private ExecutorService executor = Executors.newCachedThreadPool();
	
	public PortScan(String hostStart, String hostEnd, int portStart, int portEnd, String fileName) {
		if (portStart < 0 || portStart > 65535 || portEnd < 0 || portEnd > 65535)
			throw new IllegalArgumentException("Port range is 0-65535");
		this.hostStart = hostStart;
		this.hostEnd = hostEnd;
		this.portStart = portStart;
		this.portEnd = portEnd;
		this.fileName = fileName;
	}
	
	public void scan () {
		int hStart = Integer.parseInt(hostStart.substring(hostStart.lastIndexOf('.')+1));
		int hEnd = Integer.parseInt(hostEnd.substring(hostEnd.lastIndexOf('.')+1));
		
		for (int currentIp = hStart; currentIp <= hEnd; currentIp++) {
			String host = hostStart.substring(0, hostStart.lastIndexOf('.')+1) + currentIp;
			openPorts.put(host, new ArrayList<>());
			for (int currentPort = portStart; currentPort <= portEnd; currentPort++) {
				final int currPort = currentPort;
				executor.submit(() -> checkSocket(host, currPort));
			}
		}
		executor.shutdown();
		
		while (!executor.isTerminated());
	}
	
	public void checkSocket (String host, int port) {
		try {
			System.out.println(host + " " + port);
			Socket socket = new Socket(host, port);
			openPorts.get(host).add(port);
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeToFile () {
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(fileName), "utf-8"))) {
			for (Entry<String, ArrayList<Integer>> ports : openPorts.entrySet()) {
				writer.write(ports.getKey() + ":\n");
				for (int x = portStart; x <= portEnd; x++)
					if (ports.getValue().contains(x))
						writer.write(x + " open\n");
					else
						writer.write(x + " closed\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		PortScan scanner = new PortScan(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4]);
		scanner.scan();
		scanner.writeToFile();
	}
	
}
