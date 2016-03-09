package alda.projects;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * A treeset implemented using a self-balancing binary search tree, which in this case is an AVL tree. 
 * The tree has an associated iterator which is implemented by having references to the next largest and
 * next smallest element. As with all self-balancing binary search trees the add, contains and remove 
 * operations are gauranteed O(logN).
 * 
 * @author Sebastian Appelberg
 *
 */

public class MyTreeSet <T extends Comparable<? super T>> implements Iterable<T> {

	private TreeSetNode<T> root;
	private int size;
	private int concurrentModification;
	
	private static class TreeSetNode<T> {
		
		T data;
		int height;
		TreeSetNode<T> left;
		TreeSetNode<T> right;
		TreeSetNode<T> previous;
		TreeSetNode<T> next; 
		
		TreeSetNode (T data) {
			this.data = data;
			height = 0;
		}
	}
	
	/**
	 * Bygger upp ett nytt och tomt MyTreeSet.
	 */
	public MyTreeSet () { 
	}
	
	/**
	 * Kontrollerar om det angivna elementet finns i m�ngden. 
	 * @param element to check for
	 * @return true if this set contains element
	 */
	public boolean contains(T element) {
		if (getNode(element) != null)
			return true;
		return false;
	}
	
	/**
	 * Returnerar antalet element i denna m�ngd. 
	 * @return antalet element i detta tr�d.
	 */
	public int size () {
		return size;
	}
	
	/**
	 * Returnerar ett heltal som representerar antalet steg fr�n det l�v som ligger l�ngst fr�n
	 *roten till sj�lva roten. Antalet steg representerar h�jden p� tr�det. 
	 * @return h�jden p� tr�det.
	 */
	
	public boolean isEmpty () {
		return root == null;
	}
	
	/**
	 * T�mmer tr�det helt och h�llet och s�tter d�rav size till 0.
	 */
	public void clear () {
		root = null; 
		size = 0;
		concurrentModification = 0;
	}
	
	/**
	 * H�jden �r antalet steg till roten fr�n den nod som ligger allra l�ngst fr�n roten. 
	 * @return en int som motsvarar antalet steg till roten fr�n det mest avl�gsna elementet. 
	 */
	public int height () {
		return root.height;
	}
	
	/**
	 * L�gger till det angiva elementet i tr�det om det inte redan finns. 
	 * @param element elementet som ska l�ggas till
	 * @return false om elementet redan finns i tr�det annars returnerar den true.
	 * @exception NullPointerException om det angivna elementet �r null.
	 */
	public boolean add(T element) {
		if (element == null)
			throw new NullPointerException();
		if (contains(element))
			return false;
		if (root == null)
			root = new TreeSetNode<T>(element);
		else 
			add(element, root);
		size++;
		concurrentModification++;
		return true;
	}
	
	/**
	 * Denna metod anv�nds som st�d till den publika metoden add. 
	 * L�gger till det angivna elementet i tr�det och uppdaterar �ven ber�rda noders next-pekare och 
	 * previous-pekare. 
	 * @param element elementet som ska l�ggas till i tr�det
	 * @param node noden som elementet ska l�ggas till p�
	 */
	private void add(T element, TreeSetNode<T> node) { 
		ArrayList<TreeSetNode<T>> nodes = new ArrayList<>(); 
		for (;;) {
			nodes.add(node);
			if (element.compareTo(node.data) < 0 ) {
				if (node.left != null)
					node = node.left; 
				else {
					node.left = new TreeSetNode<T>(element);
					linkedLeftAdd(node, node.left);
					break;
				}
			} else if (element.compareTo(node.data) > 0) {
				if (node.right != null) 
					node = node.right; 
				else {
					node.right = new TreeSetNode<T>(element);
					linkedRightAdd(node, node.right);
					break;
				}
			}
		}
		
		updateHeights(nodes);
		for (int x = nodes.size()-1; x >= 0; x--)
			if (balanceFactor(nodes.get(x)) >= 2 || balanceFactor(nodes.get(x)) <= -2) 
				if (x == 0)
					balance(null, nodes.get(x));
				else
					balance(nodes.get(x-1), nodes.get(x));
		updateHeights(nodes);
	}
	
	private void linkedRightAdd(TreeSetNode<T> node, TreeSetNode<T> newNode) {
		newNode.previous = node;
		newNode.next = node.next;
		node.next = newNode;
		if (newNode.next != null) {
			newNode.next.previous = newNode;
		}
	}
	
	private void linkedLeftAdd(TreeSetNode<T> node, TreeSetNode<T> newNode) {
		newNode.next = node;
		newNode.previous = node.previous;
		node.previous = newNode;
		if (newNode.previous != null) {
			newNode.previous.next = newNode;
		}
	}
	
	private void linkedRemove (TreeSetNode<T> node) {
		if (node.previous != null) 
			node.previous.next = node.next;
		if (node.next != null) 
			node.next.previous = node.previous;
	}
	
	/**
	 * Tar bort elementet ur tr�det och g�r eventuella rotationer efter behov. Flyttar om referenser f�r liststrukturen 
	 * till n�sta st�rre och n�sta mindre element.
	 * Operation �r garanterad att utf�ras i O(logN) d�r N �r antalet element i tr�det. 
	 * @param element elementet som ska tas bort. 
	 * @return true om elementet togs bort och false om elementet inte fanns och d�rav inte togs bort.
	 * @exception NullPointerException om det angivna elementet �r null. 
	 */
	public boolean remove (T element) { 
		if (element == null)
			throw new NullPointerException();
		TreeSetNode<T> node = getNode(element);
		if (node == null)
			return false;
		size--;
		concurrentModification++;
		
		if (node.right != null && node.left != null) { 
			TreeSetNode<T> temp = findMin(node.right); 
			linkedRemove(temp);
			if (temp.equals(node.right)) { //Kollar om den n�sta i storleksordning �r precis till h�ger.
				node.data = temp.data;
				node.right = temp.right;
			} else {
				TreeSetNode<T> parent = getParent(temp.data);
				node.data = temp.data;
				parent.left = temp.right != null ? temp.right : null; 
			} 
		} else if (node.right != null || node.left != null) { 
			TreeSetNode<T> parent = getParent(element);
			linkedRemove(node);
			if (parent == null) 
				root = node.right != null ? node.right : node.left;
			else if (isRightChild(parent, node))
				parent.right = node.right != null ? node.right: node.left;
			else 
				parent.left = node.left != null ? node.left : node.right;
		} else {
			TreeSetNode<T> parent = getParent(element);
			if (parent == null) {
				clear();
				return true;
			}
			linkedRemove(node);
			if (isRightChild(parent, node))
				parent.right = null;
			else 
				parent.left = null;
		}
		
		ArrayList<TreeSetNode<T>> nodesToCheck = createUpdatePath(node.data);
		updateHeights(nodesToCheck);
		for (int x = nodesToCheck.size()-1; x >= 0; x--)
			if (balanceFactor(nodesToCheck.get(x)) >= 2 || balanceFactor(nodesToCheck.get(x)) <= -2)
				if (x == 0)
					balance(null, nodesToCheck.get(x));
				else
					balance(nodesToCheck.get(x-1), nodesToCheck.get(x));
		return true;
	}
	
	private TreeSetNode<T> getNode (T element) {
		for (TreeSetNode<T> node = root; node != null;
				node = element.compareTo(node.data) > 0 ? node.right : node.left)
			if (element.equals(node.data))
				return node;
		return null;
	}
	
	/**
	 * @param element elementet i den nod vars f�r�lder som s�ks.
	 * @param node
	 * @return TreeSetNode&lt;T&gt; om noden har en f�r�lder annars returneras null vilket indikerar att 
	 * elementet tillh�r rotnoden. 
	 */
	private TreeSetNode<T> getParent (T element) {
		for (TreeSetNode<T> node = root; node != null; 
				node = element.compareTo(node.data) > 0 ? node.right : node.left) {
			if (node.right != null && element.equals(node.right.data)) 
				return node;										
			if (node.left != null && element.equals(node.left.data)) 
				return node;										
		}
		return null;
	}
	
	/**
	 * Anv�nds f�r att leta fram det minsta elementet n�bart fr�n node. 
	 * @param node roten till det tr�d eller subtr�d som ska letas igenom.
	 * @return den minsta noden n�bar fr�n node.
	 */
	private TreeSetNode<T> findMin (TreeSetNode<T> node) {
		if (node == null)
			return null;
		while (node.left != null)
			node = node.left;
		return node;
	}
	
	private ArrayList<TreeSetNode<T>> createUpdatePath (T element) {
		ArrayList<TreeSetNode<T>> nodes = new ArrayList<>(); 
		for (TreeSetNode<T> node = root; node != null;
				node = element.compareTo(node.data) > 0 ? node.right : node.left)
			nodes.add(node);
		return nodes;
	}
	
	private void updateHeights (List <TreeSetNode<T>> nodes) {
		for (int x = nodes.size()-1; x >= 0; x--)
			nodes.get(x).height = maxHeightOfChildren(nodes.get(x)) + 1;
	}
	
	/**
	 * Anv�nds framf�rallt f�r att st�dja de metoder som uppdaterar noders h�jd. 
	 * @param node den nod vars barns h�jd j�mf�rs.
	 * @return Returnerar h�jden f�r det barn till node som �r h�gst.
	 */
	private int maxHeightOfChildren (TreeSetNode<T> node) {
		int right = node.right != null ? node.right.height : -1;
		int left = node.left != null ? node.left.height : -1;
		return Math.max(right, left);
	}
	
	private void balance (TreeSetNode<T> parent, TreeSetNode<T> node) {
		if (isRightHeavy(node) && (isRightHeavy(node.right) || isEven(node.right)))
			leftRotateOnRightChild(parent, node);
		else if (isRightHeavy(node) && isLeftHeavy(node.right))
			rightLeftRotate(parent, node);
		else if (isLeftHeavy(node) && (isLeftHeavy(node.left) || isEven(node.left)))
			rightRotateOnLeftChild(parent, node);
		else if (isLeftHeavy(node) && isRightHeavy(node.left))
			leftRightRotate(parent, node);
	}
	
	private void leftRotateOnRightChild (TreeSetNode<T> grandParent, TreeSetNode<T> parent) {
		TreeSetNode<T> node = parent.right;
		parent.right = node.left;
		node.left = parent;
		if (grandParent != null)
			if (isRightChild(grandParent, parent))
				grandParent.right = node;
			else
				grandParent.left = node;
		else
			root = node;
		updateHeights(createUpdatePath(parent.data)); 
	}
	
	private void rightRotateOnLeftChild (TreeSetNode<T> grandParent, TreeSetNode<T> parent) {
		TreeSetNode<T> node = parent.left;
		parent.left = node.right;
		node.right = parent;
		if (grandParent != null)
			if (isRightChild(grandParent, parent))
				grandParent.right = node;
			else
				grandParent.left = node;
		else
			root = node;
		updateHeights(createUpdatePath(parent.data)); 
	}
	
	private void leftRightRotate (TreeSetNode<T> grandParent, TreeSetNode<T> parent) {
		TreeSetNode<T> node = parent.left;
		leftRotateOnRightChild(parent, node);
		rightRotateOnLeftChild(grandParent, parent);
	}
	
	private void rightLeftRotate (TreeSetNode<T> grandParent, TreeSetNode<T> parent) {
		TreeSetNode<T> node = parent.right;
		rightRotateOnLeftChild(parent, node);
		leftRotateOnRightChild(grandParent, parent);
	}
	
	private int balanceFactor (TreeSetNode<T> node) {
		int right = node.right != null ? node.right.height : -1;
		int left = node.left != null ? node.left.height : -1;
		return right-left;
	}
	
	/**
	 * Kollar om nodens h�gra subtr�d �r h�gre �n dess v�nstra. 
	 * @param node den nod vars subtr�ds h�jd ska j�mf�ras.
	 * @return true om nodens h�gra subtr�d �r h�gre �n dess v�nstra. 
	 */
	private boolean isRightHeavy (TreeSetNode<T> node) { 
		int right = node.right != null ? node.right.height : -1;
		int left = node.left != null ? node.left.height : -1;
		return right > left;
	}
	
	private boolean isLeftHeavy (TreeSetNode<T> node) {
		int right = node.right != null ? node.right.height : -1;
		int left = node.left != null ? node.left.height : -1;
		return left > right;
	}
	
	private boolean isEven (TreeSetNode<T> node) {
		int right = node.right != null ? node.right.height : -1;
		int left = node.left != null ? node.left.height : -1;
		return right == left;
	}
	
	/**
	 * St�dmetod som anv�nds f�r att avg�ra om child �r ett h�gerbarn till parent.
	 * Har anv�ndning vid flera fall d� det �r viktigt att avg�ra vilken sida noden ligger om f�r�ldernoden.
	 * @param parent
	 * @param child
	 * @return Returnerar true om child �r ett h�gerbarn till parent.
	 */
	private boolean isRightChild (TreeSetNode<T> parent, TreeSetNode<T> child) {
		if (child == null || parent.right == null)
			return false;
		if (parent.right.data.equals(child.data))
			return true;
		return false;
	}
	
	/**
	 * Returnerar en iterator som itererar �ver elementen i r�tt ordning. Iteratorn utnyttjar att MyTreeSet har
	 * implementerats genom att l�ta varje nod ha tv� extra referenser, en till den nod med det n�sta elementet 
	 * i storleksordningen och en till den med det tidigare elementet i storleksordningen. Detta g�r det m�jligt 
	 * att iterera �ver hela tr�det med en k�rtid p� O(N). Iteratorn �r fail-fast p� det vis att om den manipuleras
	 * av n�got annat �n iteratorn efter det att iteratorn har skapats s� kastar iteratorn ett ConcurrentModificationException.
	 * 
	 * @return en iterator f�r elementen i MyTreeSet
	 * @exception ConcurrentModificationException om iteratorns next() eller remove() efter att tr�det manipulerats 
	 * p� andra v�gar �n genom iteratorns remove() efter att iteratorn har skapats. 
	 * @exception NoSuchElementException om next() anropas d� det inte finns 
	 * ett n�sta element, d.v.s. d� hasNext() returnerar falskt.
	 */
	@Override
	public Iterator<T> iterator() {
		Iterator<T> it = new Iterator<T>() {
			
			private TreeSetNode<T> next = findMin(root);
			private T currentValue;
			private int concurrMod = concurrentModification;
			private boolean nextUsed = false;
			
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public T next() {
				if (!hasNext())
					throw new NoSuchElementException();
				if (concurrMod != concurrentModification)
					throw new ConcurrentModificationException();
				currentValue = next.data;
				next = next.next;
				nextUsed = true;
				return currentValue;
			}
			
			@Override
			public void remove() {
				if (!nextUsed)
					throw new IllegalStateException();
				if (concurrMod != concurrentModification)
					throw new ConcurrentModificationException();
				MyTreeSet.this.remove(currentValue);
				nextUsed = false;
				concurrMod++;
			}
		};
		return it;
	}
	
	/**
	 * Anv�nder StringBuilder och den egna klassens iterator f�r att bygga str�ngen. Str�ngen har f�ljande 
	 * notation: [T, T1, T3...]. 
	 * 
	 * @return en str�ngreprentation av datasamlingen. 
	 */
	public String toString () {
		Iterator<T> it = iterator();
		StringBuilder str = new StringBuilder();
		str.append("[");
		while (it.hasNext()) {
			str.append(it.next());
			if (it.hasNext())
				str.append(", ");
		}
		str.append("]");
		return str.toString();
	}
	
}
