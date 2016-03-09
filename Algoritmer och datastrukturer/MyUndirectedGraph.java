//Sebastian Appelberg
//sebastian.appelberg@hotmail.com
package alda.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MyUndirectedGraph <T> implements UndirectedGraph<T> {
	
	private HashMap<T, List<Edge<T>>> graph = new HashMap<>();
	private int numberOfEdges;

	@Override
	public int getNumberOfNodes() {
		return graph.keySet().size();
	}

	@Override
	public int getNumberOfEdges() {
		return numberOfEdges;
	}

	@Override
	public boolean add(T newNode) {
		if (graph.containsKey(newNode))
			return false;
		graph.put(newNode, new ArrayList<Edge<T>>());
		return true;
	}

	@Override
	public boolean connect(T node1, T node2, int cost) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2) || cost <= 0)
			return false;
		if (!isConnected(node1, node2)) {
			graph.get(node1).add(new Edge<T>(node2, cost));
			graph.get(node2).add(new Edge<T>(node1, cost));
		} else {
			getEdgeBetween(node1, node2).setWeight(cost);
			getEdgeBetween(node2, node1).setWeight(cost);
		}
		numberOfEdges++;
		return true;
	}
	
	private Edge<T> getEdgeBetween (T node1, T node2) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2))
			return null;
		for (Edge<T> edge : graph.get(node1)) 
			if (edge.getDestination().equals(node2))
				return edge;
		return null;
	}
	
	@Override
	public boolean isConnected(T node1, T node2) {
		if (!graph.containsKey(node1) || !graph.containsKey(node2))
			return false;
		for (Edge<T> edge : graph.get(node1))
			if (edge.getDestination().equals(node2))
				return true;
		return false;
	}
	
	@Override
	public int getCost(T node1, T node2) {
		if (!isConnected(node1, node2) || !graph.containsKey(node1) || !graph.containsKey(node2))
			return -1;
		int cost = 0;
		for (Edge<T> edge : graph.get(node1))
			if (edge.getDestination().equals(node2))
				cost = edge.getWeight();
		return cost;
	}
	
	@Override
	public List<T> depthFirstSearch(T start, T end) {
		if (!graph.containsKey(start) || !graph.containsKey(end))
			return new ArrayList<>();
		HashMap<T, T> through = new HashMap<>();
		depthFirstSearch(start, end, through);
		return makePath(start, end, through);
	}
	
	private void depthFirstSearch(T start, T end, Map<T, T> through) {
		for (Edge<T> edge : graph.get(start)) {
			if (!through.containsKey(edge.getDestination())) {
				through.put(edge.getDestination(), start);
				depthFirstSearch(edge.getDestination(), end, through);
			}
		}
	}
	
	private List<T> makePath (T start, T end, Map<T, T> through) {
		ArrayList<T> nodePath = new ArrayList<>(through.size());
		while (!end.equals(start)) {
			T prev = through.get(end);
			nodePath.add(end);
			end = prev;
		}
		nodePath.add(start);
		Collections.reverse(nodePath);
		return nodePath;
	}

	@Override
	public List<T> breadthFirstSearch(T start, T end) {
		ArrayDeque<T> queue = new ArrayDeque<>();
		Map<T, T> through = new HashMap<>();
		queue.add(start);
		while (!queue.isEmpty()) {
			T next = queue.remove();
			for (Edge<T> edge : graph.get(next)) {
				if (!through.containsKey(edge.getDestination())) {
					through.put(edge.getDestination(), next);
					queue.add(edge.getDestination());
				}
			}
			if (next.equals(end))
				break;
		}
		if (!through.containsKey(start) || !through.containsKey(end))
			return new ArrayList<>();
		return makePath(start, end, through);
	}

	@Override
	public UndirectedGraph<T> minimumSpanningTree() {
		if (getNumberOfNodes() < 1)
			return new MyUndirectedGraph<>();
		HashMap<T, Boolean> searched = new HashMap<>();
		HashMap<T, Integer> weights = new HashMap<>();
		HashMap<T, T> path = new HashMap<>();
		for (T node : graph.keySet()) {
			if ((graph.get(node).size() > 0 && !isConnected(node, node)) || graph.get(node).size() > 2) {
				searched.put(node, false);
				weights.put(node, Integer.MAX_VALUE);
				path.put(node, null);
			}
		}
		
		@SuppressWarnings("unchecked")
		T startNode = (T) searched.keySet().toArray()[0];
		
		searched.put(startNode, true);
		weights.put(startNode, 0);
		T where = startNode;
		while (searched.containsValue(false)) {
			for (Edge<T> edge : graph.get(where)) {
				T dest = edge.getDestination();
				int weight = edge.getWeight();
				if (weight < weights.get(dest) && !searched.get(dest) && !where.equals(dest)) {
					path.put(dest, where);
					weights.put(dest, weight);
				}
			}
			searched.put(where, true);
			int smallest = Integer.MAX_VALUE;
			for (Entry<T, Integer> entry : weights.entrySet()) {
				if (entry.getValue() < smallest && !searched.get(entry.getKey())) {
					smallest = entry.getValue();
					where = entry.getKey();
				}
			}
		}
		return pathToMST(path);
	}
	
	private UndirectedGraph<T> pathToMST (Map<T, T> path) {
		UndirectedGraph<T> minSpannTree = new MyUndirectedGraph<T>();
		for (Entry<T, T> entry : path.entrySet()) {
			minSpannTree.add(entry.getKey());
			if (entry.getValue() != null)
				minSpannTree.add(entry.getValue());
			minSpannTree.connect(entry.getKey(), entry.getValue(), getCost(entry.getKey(), entry.getValue()));
		}
		return minSpannTree;
	}
	
}
