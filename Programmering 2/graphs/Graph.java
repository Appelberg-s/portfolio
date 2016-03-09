package graphs;

import java.util.List;
import java.util.Set;


public interface Graph <E> {
	
	void add(E node);
	void connect(E from, E to, String name, int weight);
	void setConnectionWeight(E node1, E node2, int weight);
	Set<E> getNodes();
	List<Edge<E>> getEdgesFrom(E node);
	Edge<E> getEdgeBetween(E node1, E node2);
	void disconnect(E from, E to);
	boolean remove(E node);	
	int size();

}
