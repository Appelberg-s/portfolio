//Sebastian
package alda.linear;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class SinglyLinkedList<E> implements ALDAList<E> {
	
	private Node<E> first;
	private Node<E> last;
	private int size;
	private int concModifcation = 0;
	
	@Override
	public Iterator<E> iterator() {
		Iterator<E> it = new Iterator<E>() {
			private boolean nextUsed = false;
			private E next;
			private Node<E> current = first;
			private int modCount = concModifcation;
			
			@Override
			public boolean hasNext() {
				return current != null;
			}

			@Override
			public E next() {
				if (!hasNext())
					throw new NoSuchElementException();
				if (modCount != concModifcation)
					throw new ConcurrentModificationException();
				next = current.data;
				current = current.next;
				nextUsed = true;
				return next;
			}
			
			public void remove () {
				if (!nextUsed)
					throw new IllegalStateException();
				if (modCount != concModifcation)
					throw new ConcurrentModificationException();
				SinglyLinkedList.this.remove(next);
				nextUsed = false;
				modCount++;
			}
		};
		return it;
	}

	@Override
	public void add(E element) {
		if (first == null) {
			first = new Node<E>(element);
			last = first;
		} else {
			last.next = new Node<E>(element);
			last = last.next;
		} 
		size++;
		concModifcation++;
	}
	
	@Override
	public void add(int index, E element) {
		if (index < 0 || index > size())
			throw new IndexOutOfBoundsException();
		Node<E> node = new Node<>(element);
		if (index == size) {
			add(element);
		} else if (index == 0 && size() > 0) {
			node.next = first;
			first = node;
			size++;
		} else {
			node.next = getNode(index);
			getNode(index-1).next = node;
			size++;
		}
		concModifcation++;
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		E element = null;
		if (index == 0) {
			element = first.data;
			first = first.next;
		} else if (index == size()-1) {
			Node <E> temp = getNode(index-1);
			element = temp.next.data;
			temp.next = null; 
			last = temp;
		} else {			
			Node <E> temp = getNode(index-1);
			element = temp.next.data;
			temp.next = temp.next.next;			
		}
		size--;
		concModifcation++;
		return element;
	}

	@Override
	public boolean remove(E element) {
		int index = indexOf(element);
		if (index == -1)
			return false;
		return remove(index) != null;
	} 
	
	@Override
	public E get(int index) {
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		return getNode(index).data;
	}

	@Override
	public boolean contains(E element) {
		return indexOf(element) != -1;
	}
	
	@Override
	public int indexOf(E element) {
		int index = 0;
		for (E other : this) {
			if (other == element || other.equals(element))
				return index;
			index++;
		}
		return -1;
	}
	
	@Override
	public void clear() {
		first = null;
		last = null;
		size = 0;
		concModifcation++;
	}

	@Override
	public int size() {
		return size;
	}
	
	public String toString () {
		String str = "[";
		Iterator<E> iterator = iterator();
		while (iterator.hasNext()) {
			str += iterator.next();
			if (iterator.hasNext())
				str += ", ";
		}
		str += "]";
		return str;
	}
	
	private Node<E> getNode(int index) {
		Node<E> temp = first;
		for (int x = 0; x < index; x++)
			temp = temp.next;
		return temp;
	}
	
	private static class Node<E> {
		E data;
		Node<E> next;
		
		public Node(E data) {
			this.data = data;
		}
	}
	
}