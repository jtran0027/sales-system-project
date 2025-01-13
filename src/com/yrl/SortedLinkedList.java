package com.yrl;

import java.util.Iterator;
import java.util.Collection;
import java.util.Comparator;


/**
 * Node-based sorted linked list that when created requires a comparator.
 * The list is automatically sorted every time an item is added.
 * 
 * @author jason
 *
 * @param <T>
 */
public class SortedLinkedList<T>  implements Iterable<T> {
	
	private Node<T> head;
	private int size;
	private Comparator<T> comparator;
	
	//Constructor that needs a comparator so the list stays sorted.
	public SortedLinkedList(Comparator<T> comparator) {
		this.head = null;
		this.size = 0;
		this.comparator = comparator;
	}
	
	
	public int getSize() {
		return this.size;
	}


	public void clear() {
		this.head = null;
		this.size = 0;
	}

	/**
	 * Adds an element to the list and sorts it based on the comparator.
	 * 
	 * @param element
	 */
	public void add(T element) {

	    Node<T> newNode = new Node<T>(element);
	    
	    //Checks if the list is empty or that the new element being inserted is supposed to be above the top element
	    if (this.head == null || comparator.compare(newNode.getElement(), this.head.getElement()) <= 0) {
	        newNode.setNext(this.head);
	        this.head = newNode;
	        this.size++;
	        return;
	    }
	    
	    //Traverses the list until it sets the node after the current one based on the comparator
	    Node<T> currentNode = this.head;
	    while (currentNode.getNext() != null && comparator.compare(newNode.getElement(), currentNode.getNext().getElement()) > 0) {
	        currentNode = currentNode.getNext();
	    }
	    newNode.setNext(currentNode.getNext());
	    currentNode.setNext(newNode);
	    this.size++;
	}
	
	/**
	 * Adds any type of collection to the LinkedList
	 * 
	 * @param elements
	 */
	public void batchAdd(Collection<T> elements) {
		for(T element : elements) {
			add(element);
		}
	}

	/**
	 * Removes an element based on the position given
	 * 
	 * @param position
	 */
	public void remove(int position) {
		
		//Check if empty
	    if (size == 0) {
	        throw new IllegalStateException("List is empty.");
	    }
		
		this.boundsCheck(position);
		
		//Remove the first element and then make the next one the new head
		if(position == 0) {
			this.head = this.head.getNext();
		} else {
			Node<T> previous = this.getNode(position-1);
			Node<T> current = previous.getNext();
			previous.setNext(current.getNext());
		}
		this.size--;
	}

	/**
	 * Returns a node at the requested position given
	 * 
	 * @param position
	 * @return
	 */
	private Node<T> getNode(int position) {
		this.boundsCheck(position);
		
		Node<T> currentNode = this.head;
		for(int i=0; i<position; i++) {
			currentNode = currentNode.getNext();
		}
		return currentNode;
	}

	/**
	 * Returns an element at the given position
	 * 
	 * @param position
	 * @return
	 */
	public T getElement(int position) {
		this.boundsCheck(position);
		
		return this.getNode(position).getElement();
	}

	
	/**
	 * Checks if the given position is in bounds
	 * 
	 * @param position
	 */
	private void boundsCheck(int position) {
		if(position < 0 || position > this.size ) {
			throw new IllegalArgumentException("Invalid index: " + position);
		} 
	}
	
	
	//Implementation of the iterator method from the Iterator class
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node<T> curr = head;
			
			@Override
			public boolean hasNext() {
				return curr != null;
			}
			
			@Override
			public T next() {
				T element = curr.getElement();
				curr = curr.getNext();
				return element;
			}
		};
	}
	
}
