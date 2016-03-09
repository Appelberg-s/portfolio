import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

/**
 * Uppgift 3.4.1 XML-RPC på klientsidan
 * 
 * @author Sebastian Appelberg
 *
 */
public class RpcClient {
	
	private XmlRpcClient client;
	private String response;
	
	public RpcClient (String host, int port, String password) {
		try {
			client = new XmlRpcClient(host, port);
			Vector<String> params = new Vector<>(2);
			params.add(password);
			response = (String) client.execute("prime_handler.getPrime", params);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (XmlRpcException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getResponse() {
		return response;
	}
	
	public static void main(String[] args) {
		RpcClient client = new RpcClient(args[0], Integer.parseInt(args[1]), args[2]);
		System.out.println(client.getResponse());
	}

}
