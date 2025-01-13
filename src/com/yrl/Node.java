package com.yrl;

/**
 * Data storage used by MyLinkedList. Contains the necessary information
 * needed for a list structure.
 * 
 * @author jason
 *
 * @param <T>
 */
public class Node<T> {

	private Node<T> next;
	private T element;

	public Node(T element) {
		this.element = element;
		this.next = null;
	}
	
	public void setElement(T element) {
		this.element = element;
	}

	public T getElement() {
		return this.element;
	}

	public Node<T> getNext() {
		return next;
	}

	public void setNext(Node<T> next) {
		this.next = next;
	}

	public String toString() {
		return this.element.toString();
	}

}
