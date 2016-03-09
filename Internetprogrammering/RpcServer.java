import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

import org.apache.xmlrpc.WebServer;

/**
 * Uppgift 3.4.2 XML-RPC på serversidan
 * 
 * @author Sebastian Appelberg
 *
 */
public class RpcServer {
	
	public RpcServer () {
	}
	
	public String getPrime (String password) {
		if (!password.equals("2isAnOddPrime"))
			return "Wrong password!";
		BigInteger prime = new BigInteger(30, 0, new Random());
		while (!isPrime(Long.parseLong(prime.toString())))
			prime = new BigInteger(30, 0, new Random());
		Date date = new Date();
		return "Prime: " + prime.toString() + "\nFound: " + date;
	}
	
	private boolean isPrime (long candidate) {
		long sqrt = (long) Math.sqrt(candidate);
		for (long i = 3; i <= sqrt; i+=2)
			if (candidate % i == 0)
				return false;
		return true;
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer(Integer.parseInt(args[0]));
		server.addHandler("prime_handler", new RpcServer());
		server.start();
	}

}
