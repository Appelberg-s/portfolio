package alda.graph;

import java.util.List;

public interface UndirectedGraph<T> {

	/**
	 * Antalet noder i grafen.
	 * 
	 * @return antalet noder i grafen.
	 */
	int getNumberOfNodes();

	/**
	 * Antalet b�gar i grafen.
	 * 
	 * @return antalet b�gar i grafen.
	 */
	int getNumberOfEdges();

	/**
	 * L�gger till en ny nod i grafen.
	 * 
	 * @param newNode
	 *            datat f�r den nya noden som ska l�ggas till i grafen.
	 * @return false om noden redan finns.
	 */
	boolean add(T newNode);

	/**
	 * Kopplar samman tv� noder i grafen. Eftersom grafen �r oriktad s� spelar
	 * det ingen roll vilken av noderna som st�r f�rst. Det �r ocks�
	 * fullst�ndigt okej att koppla ihop en nod med sig sj�lv. D�remot till�ts
	 * inte multigrafer. Om tv� noder f�rs�ks kopplas ihop som redan �r
	 * ihopkopplade uppdateras bara deras kostnadsfunktion.
	 * 
	 * @param node1
	 *            den ena noden.
	 * @param node2
	 *            den andra noden.
	 * @param cost
	 *            kostnaden f�r att ta sig mellan noderna. Denna m�ste vara >0
	 *            f�r att noderna ska kunna kopplas ihop.
	 * @return true om b�gge noderna finns i grafen och kan kopplas ihop.
	 */
	boolean connect(T node1, T node2, int cost);

	/**
	 * Ber�ttar om tv� noder �r sammanbundan av en b�ge eller inte.
	 * 
	 * @param node1
	 *            den ena noden.
	 * @param node2
	 *            den andra noden.
	 * @return om noderna �r sammanbundna eller inte.
	 */
	boolean isConnected(T node1, T node2);

	/**
	 * Returnerar kostnaden f�r att ta sig mellan tv� noder.
	 * 
	 * @param node1
	 *            den ena noden.
	 * @param node2
	 *            den andra noden.
	 * @return kostnaden f�r att ta sig mellan noderna eller -1 om noderna inte
	 *         �r kopplade.
	 */
	int getCost(T node1, T node2);

	/**
	 * G�r en djupet-f�rst-s�kning efter en v�g mellan tv� noder.
	 * 
	 * Observera att denna metod inte anv�nder sig av viktinformationen.
	 * 
	 * @param start
	 *            startnoden.
	 * @param end
	 *            slutnoden.
	 * @return en lista �ver alla noder p� v�gen mellan start- och slutnoden. Om
	 *         ingen v�g finns �r listan tom.
	 */
	List<T> depthFirstSearch(T start, T end);

	/**
	 * G�r en bredden-f�rst-s�kning efter en v�g mellan tv� noder.
	 * 
	 * Observera att denna metod inte anv�nder sig av viktinformationen. Ni ska
	 * allts� inte implementera Dijkstra eller A*.
	 *
	 * @param start
	 *            startnoden.
	 * @param end
	 *            slutnoden.
	 * @return en lista �ver alla noder p� v�gen mellan start- och slutnoden. Om
	 *         ingen v�g finns �r listan tom.
	 */
	List<T> breadthFirstSearch(T start, T end);

	/**
	 * Returnerar en ny graf som utg�r ett minimalt sp�nnande tr�d till grafen.
	 * Ni kan f�ruts�tta att alla noder ing�r i samma graf.
	 * 
	 * @return en graf som representerar ett minimalt sp�nnande tr�d.
	 */
	UndirectedGraph<T> minimumSpanningTree();
}
