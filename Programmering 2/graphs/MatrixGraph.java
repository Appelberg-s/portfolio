package graphs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class MatrixGraph <E> implements Graph <E>, Serializable {

	private Edge<E>[][] graph;
	private Map<E, Integer> index = new HashMap<>();
	private int size;
	private boolean dynamic;
	
	@SuppressWarnings("unchecked")
	public MatrixGraph(int initCap) {
		graph = (Edge<E>[][]) new Edge<?>[initCap][initCap];
	}
	
	@SuppressWarnings("unchecked")
	public MatrixGraph() {
		graph = (Edge<E>[][]) new Edge<?>[1][1];
		this.dynamic = true;
	}
	
	@Override
	public void add(E node) {
		if (index.containsKey(node))
			throw new IllegalArgumentException("Node already exists");
		if (dynamic) {
			if (size+1 > graph.length) 
				increaseCapacity();
			else if (size*2+1 < graph.length)
				decreaseCapacity();				 
		} else if (size+1 > graph.length)
				throw new IndexOutOfBoundsException();
		index.put(node, index.size());
		size++;
	}
	
	@Override
	public void connect(E from, E to, String name, int weight) {
		if (!index.containsKey(from) || !index.containsKey(to)) 
			throw new NoSuchElementException("Node not found");
		if (getEdgeBetween(from, to) != null || getEdgeBetween(to, from) != null)
			throw new IllegalStateException("Nodes are already connected");
		int fromNr = index.get(from);
		int toNr = index.get(to);
		Edge<E> e1 = new Edge<>(to, name, weight);
		graph[fromNr][toNr] = e1;
		Edge<E> e2 = new Edge<>(from, name, weight);
		graph[toNr][fromNr] = e2;
	}

	@Override
	public void setConnectionWeight (E from, E to, int weight) {
		if (!index.containsKey(from) || !index.containsKey(to) || !GraphMethods.pathExists(this, from, to))
			throw new NoSuchElementException();
		graph[index.get(from)][index.get(to)].setWeight(weight);
		graph[index.get(to)][index.get(from)].setWeight(weight);
	}
	
	@Override
	public Set<E> getNodes() {
		return new HashSet<>(index.keySet());
	}
	
	@Override
	public List<Edge<E>> getEdgesFrom(E node) {
		if (!index.containsKey(node))
			throw new NoSuchElementException();
		List<Edge<E>> edges = new ArrayList<Edge<E>>();
		for (int x = 0; x < graph.length; x++) {
			if (graph[index.get(node)][x] != null)
				edges.add(graph[index.get(node)][x]);
		}
		return edges;
	}
	
	@Override
	public Edge<E> getEdgeBetween(E from, E to) {
		if (!index.containsKey(from) || !index.containsKey(to)) 
			throw new NoSuchElementException(); 
		return graph[index.get(from)][index.get(to)];
	}
	
	@Override
	public String toString () {
		String str = "";
		for (Entry<E, Integer> e : index.entrySet()) {
			str += "Node(" + e.getKey() + ")";
			for (int x = 0; x < graph[e.getValue()].length; x++) {
				if (graph[e.getValue()][x] != null)
					str += ", " + graph[e.getValue()][x]; 
			}
			str += "\n";
		}
		return str;
	}

	@Override
	public boolean remove(E node) {
		if (index.containsKey(node)) {
			for (int connectedNode = 0; connectedNode < size; connectedNode++) {
				graph[index.get(node)][connectedNode] = null;
				graph[connectedNode][index.get(node)] = null;
			}
			index.remove(node);
			size--;
			return true;
		}
		return false;
	}

	@Override
	public void disconnect(E from, E to) {
		if (!index.containsKey(from) || !index.containsKey(to))
			throw new NoSuchElementException();
		graph[index.get(from)][index.get(to)] = null;
		graph[index.get(to)][index.get(from)] = null;
	}
	
	@Override
	public int size() {
		return size;
	}
	
	public int capacity() {
		return graph.length;
	}
	
	@SuppressWarnings("unchecked")
	private void increaseCapacity() {
		Edge<E>temp[][] = graph;
		graph = (Edge<E>[][]) new Edge<?>[graph.length*2][graph.length*2];
		for (int x = 0; x > temp.length; x++) {
			for (int y = 0; y < temp[y].length; y++) {
				graph[x][y] = temp[x][y];
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void decreaseCapacity () {
		Edge<E>temp[][] = graph;
		graph = (Edge<E>[][]) new Edge<?>[graph.length/2][graph.length/2];
		for (int x = 0; x > temp.length; x++) {
			for (int y = 0; y < temp[y].length; y++) {
				graph[x][y] = temp[x][y];
			}
		}
	}
}
