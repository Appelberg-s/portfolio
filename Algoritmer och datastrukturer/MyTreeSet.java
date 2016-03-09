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
	 * Kontrollerar om det angivna elementet finns i mängden. 
	 * @param element to check for
	 * @return true if this set contains element
	 */
	public boolean contains(T element) {
		if (getNode(element) != null)
			return true;
		return false;
	}
	
	/**
	 * Returnerar antalet element i denna mängd. 
	 * @return antalet element i detta träd.
	 */
	public int size () {
		return size;
	}
	
	/**
	 * Returnerar ett heltal som representerar antalet steg från det löv som ligger längst från
	 *roten till själva roten. Antalet steg representerar höjden på trädet. 
	 * @return höjden på trädet.
	 */
	
	public boolean isEmpty () {
		return root == null;
	}
	
	/**
	 * Tömmer trädet helt och hållet och sätter därav size till 0.
	 */
	public void clear () {
		root = null; 
		size = 0;
		concurrentModification = 0;
	}
	
	/**
	 * Höjden är antalet steg till roten från den nod som ligger allra längst från roten. 
	 * @return en int som motsvarar antalet steg till roten från det mest avlägsna elementet. 
	 */
	public int height () {
		return root.height;
	}
	
	/**
	 * Lägger till det angiva elementet i trädet om det inte redan finns. 
	 * @param element elementet som ska läggas till
	 * @return false om elementet redan finns i trädet annars returnerar den true.
	 * @exception NullPointerException om det angivna elementet är null.
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
	 * Denna metod används som stöd till den publika metoden add. 
	 * Lägger till det angivna elementet i trädet och uppdaterar även berörda noders next-pekare och 
	 * previous-pekare. 
	 * @param element elementet som ska läggas till i trädet
	 * @param node noden som elementet ska läggas till på
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
	 * Tar bort elementet ur trädet och gör eventuella rotationer efter behov. Flyttar om referenser för liststrukturen 
	 * till nästa större och nästa mindre element.
	 * Operation är garanterad att utföras i O(logN) där N är antalet element i trädet. 
	 * @param element elementet som ska tas bort. 
	 * @return true om elementet togs bort och false om elementet inte fanns och därav inte togs bort.
	 * @exception NullPointerException om det angivna elementet är null. 
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
			if (temp.equals(node.right)) { //Kollar om den nästa i storleksordning är precis till höger.
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
	 * @param element elementet i den nod vars förälder som söks.
	 * @param node
	 * @return TreeSetNode&lt;T&gt; om noden har en förälder annars returneras null vilket indikerar att 
	 * elementet tillhör rotnoden. 
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
	 * Används för att leta fram det minsta elementet nåbart från node. 
	 * @param node roten till det träd eller subträd som ska letas igenom.
	 * @return den minsta noden nåbar från node.
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
	 * Används framförallt för att stödja de metoder som uppdaterar noders höjd. 
	 * @param node den nod vars barns höjd jämförs.
	 * @return Returnerar höjden för det barn till node som är högst.
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
	 * Kollar om nodens högra subträd är högre än dess vänstra. 
	 * @param node den nod vars subträds höjd ska jämföras.
	 * @return true om nodens högra subträd är högre än dess vänstra. 
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
	 * Stödmetod som används för att avgöra om child är ett högerbarn till parent.
	 * Har användning vid flera fall då det är viktigt att avgöra vilken sida noden ligger om föräldernoden.
	 * @param parent
	 * @param child
	 * @return Returnerar true om child är ett högerbarn till parent.
	 */
	private boolean isRightChild (TreeSetNode<T> parent, TreeSetNode<T> child) {
		if (child == null || parent.right == null)
			return false;
		if (parent.right.data.equals(child.data))
			return true;
		return false;
	}
	
	/**
	 * Returnerar en iterator som itererar över elementen i rätt ordning. Iteratorn utnyttjar att MyTreeSet har
	 * implementerats genom att låta varje nod ha två extra referenser, en till den nod med det nästa elementet 
	 * i storleksordningen och en till den med det tidigare elementet i storleksordningen. Detta gör det möjligt 
	 * att iterera över hela trädet med en körtid på O(N). Iteratorn är fail-fast på det vis att om den manipuleras
	 * av något annat än iteratorn efter det att iteratorn har skapats så kastar iteratorn ett ConcurrentModificationException.
	 * 
	 * @return en iterator för elementen i MyTreeSet
	 * @exception ConcurrentModificationException om iteratorns next() eller remove() efter att trädet manipulerats 
	 * på andra vägar än genom iteratorns remove() efter att iteratorn har skapats. 
	 * @exception NoSuchElementException om next() anropas då det inte finns 
	 * ett nästa element, d.v.s. då hasNext() returnerar falskt.
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
	 * Använder StringBuilder och den egna klassens iterator för att bygga strängen. Strängen har följande 
	 * notation: [T, T1, T3...]. 
	 * 
	 * @return en strängreprentation av datasamlingen. 
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
