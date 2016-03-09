package graphs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


public class ListGraph <E> implements Graph <E>, Serializable {
	
	private Map<E, List<Edge<E>>> graph = new HashMap<>();
	private int size;
	
	public void add(E node) {
		if (graph.containsKey(node)) 
			throw new IllegalArgumentException("Node already exists");
		graph.put(node, new ArrayList<Edge<E>>());
		size++;
	}
	
	public void connect (E from, E to, String name, int weight) {
		if (!graph.containsKey(from) || !graph.containsKey(to)) 
			throw new NoSuchElementException("Node not found");
		if (getEdgeBetween(from, to) != null || getEdgeBetween(to, from) != null)
			throw new IllegalStateException("Nodes are already connected");
		List<Edge<E>> fromList = graph.get(from);
		List<Edge<E>> toList = graph.get(to);
		Edge<E> e1 = new Edge<E>(to, name, weight);
		fromList.add(e1);
		Edge<E> e2 = new Edge<E>(from, name, weight);
		toList.add(e2);
	}
		
	@Override
	public void setConnectionWeight (E from, E to, int weight) {
		if (!graph.containsKey(from) || !graph.containsKey(to) || !GraphMethods.pathExists(this, from, to))
			throw new NoSuchElementException("At setConnectionWeight()");
		if (weight < 0)
			throw new IllegalArgumentException("Negative value for weight is not allowed");
		getEdgeBetween(from, to).setWeight(weight);
		getEdgeBetween(to, from).setWeight(weight);
	}
	
	@Override
	public Set<E> getNodes() {
		return new HashSet<E>(graph.keySet());
	}
	
	@Override
	public List<Edge<E>> getEdgesFrom (E node) {
		if (!graph.containsKey(node))
			throw new NoSuchElementException();
		return new ArrayList<Edge<E>>(graph.get(node));
	}
	
	public Edge<E> getEdgeBetween (E node1, E node2) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2))
			throw new NoSuchElementException();
		for (Edge <E> edge: graph.get(node1)) {
			if (edge.getDestination().equals(node2))
				return edge;
		}
		return null;
	}
	
	@Override
	public int size () {
		return size;
	}
	
	@Override
	public String toString () {
		String str = "";
		for (Map.Entry<E, List<Edge<E>>> e : graph.entrySet()) {
			str += "Node(" + e.getKey() + ")";
			for (Edge <E> edge : e.getValue()) {
				str += ", " + edge;
			}
			str += "\n";
		}
		return str;
	}

	@Override
	public boolean remove(E node) {
		if (!graph.containsKey(node))
			return false;
		ArrayList<E> connectionsToRemove = new ArrayList<>();
		for (Edge<E> edge : graph.get(node)) 
			connectionsToRemove.add(edge.getDestination());
		for (E connectedNode : connectionsToRemove)
			disconnect(node, connectedNode);
		graph.remove(node);
		size--;
		return true;
	}

	@Override
	public void disconnect(E from, E to) {
		if (!graph.containsKey(from) || !graph.containsKey(to))
			throw new NoSuchElementException("At disconnect");
		graph.get(from).remove(getEdgeBetween(from, to));
		graph.get(to).remove(getEdgeBetween(to, from));
	}

}
