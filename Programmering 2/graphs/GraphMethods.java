package graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GraphMethods {
	
	public static <E> boolean pathExists (Graph<E> graph, E from, E to) {
		Set<E> visited = new HashSet<>();
		depthFirstSearch(graph, from, visited);
		return visited.contains(to);
	}
	
	public static <E> List<Edge<E>> FastestPath (Graph<E> graph, E from, E to) {
		if (!pathExists(graph, from, to))
			return null;
		Set<E> connectedNodes = new HashSet<>();
		depthFirstSearch(graph, from, connectedNodes);
		
		Map<E, Boolean> searchedNodes = new HashMap<>();
		Map<E, Integer> nodeWeights = new HashMap<>();
		Map<E, E> nodePath = new HashMap<>();
		
		for (E node : connectedNodes) {
			searchedNodes.put(node, false);
			nodeWeights.put(node, Integer.MAX_VALUE);
			nodePath.put(node, null);
		}
		searchedNodes.put(from, true);
		nodeWeights.put(from, 0);
		
		E minstaNoden = from;
		
		while (searchedNodes.containsValue(false)) {
			for (Edge<E> edge : graph.getEdgesFrom(minstaNoden)) {
				int nodeWeight = nodeWeights.get(minstaNoden) + edge.getWeight();
				if (nodeWeight < nodeWeights.get(edge.getDestination())) {
					nodeWeights.put(edge.getDestination(), nodeWeight);
					nodePath.put(edge.getDestination(), minstaNoden);
				}
			}
			searchedNodes.put(minstaNoden, true);	
			int minst = Integer.MAX_VALUE;
			for (Entry<E, Integer> entry : nodeWeights.entrySet()) {
				if (entry.getValue() < minst && searchedNodes.get(entry.getKey()) != true) {
					minst = entry.getValue();
					minstaNoden = entry.getKey();
				}
			}			
		}
		List<Edge<E>> theWay = new ArrayList<>();
		E where = to;
		while (!where.equals(from)) {
			E prevDest = nodePath.get(where);
			Edge <E> e = graph.getEdgeBetween(prevDest, where);
			theWay.add(e);
			where = prevDest;
		}
		Collections.reverse(theWay);
		return theWay;
	}
	
	private static <E> void depthFirstSearch (Graph<E> graph, E where, Set<E> visited) {
		visited.add(where);
		for (Edge<E> e : graph.getEdgesFrom(where)) {
			if (!visited.contains(e.getDestination())) {
				depthFirstSearch(graph, e.getDestination(), visited);
			}
		}
	}
	
}
