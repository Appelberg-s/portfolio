//Sebastian Appelberg
//sebastian.appelberg@hotmail.com
package alda.graph;

public class Edge <T> {

	private T destination;
	private int weight;
	
	protected Edge (T destination, int weight) {
		this.destination = destination;
		this.weight = weight;
	}
	
	public int getWeight () {
		return weight;
	}
	
	public void setWeight (int weight) {
		this.weight = weight;
	}
	
	public T getDestination () {
		return destination;
	}
	
	public String toString () {
		return "To: " + destination.toString() + " Weight: " + weight;
	}
	
}
