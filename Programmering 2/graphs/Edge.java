package graphs;

import java.io.Serializable;

public class Edge <T> implements Serializable {
	
	private T destination;
	private String name;
	private int weight;
	
	protected Edge (T destination, String name, int weight) {
		if (weight < 0)
			throw new IllegalArgumentException("Negative value for weight is not allowed");
		this.name = name;
		this.weight = weight;
		this.destination = destination;
	}
	
	public void setWeight (int weight) {
		if (weight < 0)
			throw new IllegalArgumentException("Negative value for weight is not allowed");
		this.weight = weight;
	}
	
	public String getName () {
		return name;
	}
	
	public int getWeight () {
		return weight;
	}
	
	public T getDestination () {
		return destination;
	}
		
	public String toString () {
		return name + " to " + destination + " weight: " + weight; 
	}
	

}
