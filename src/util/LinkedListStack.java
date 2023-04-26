/** Generic LinkedListStack class that represents a stack data structure using a SinglyLinkedList */
public class LinkedListStack<E> {

    private SinglyLinkedList<E> list = new SinglyLinkedList<>();

    /** Constructor method of the LinkedListStack */
    public LinkedListStack() {} // LinkedListStack (constructor)

    /** 
     * Returns the number of elements in the stack. 
     * @return number of elements in calling stack
     */
    public int size() { return this.list.size(); } // size

    /** 
     * Tests whether the stack is empty. 
     * @return boolean representing if the calling stack is empty
     */
    public boolean isEmpty() { return this.list.isEmpty(); } // isEmpty

    /** Inserts an element at the top of the stack. */
    public void push(E e) { this.list.addFirst(e); } // push

    /** 
     * Returns, but does not remove, the element at the top of the stack.
     * @return top element of the calling stack
     */
    public E top() { return this.list.first(); } // top

    /** 
     * Removes and returns the top element from the stack. 
     * @return element removed from the calling stack
     */
    public E pop() { return this.list.removeFirst(); } // pop

	/** 
     * Returns a LinkedListStack containing all of the items of the input String delimited by a space 
     * @return new stack containing all items of the input String, with the beginning of the String as the top element
     */
    public static LinkedListStack<String> stringToStack(String expression) { 
        String[] splitStrings = expression.split(" ");
        LinkedListStack<String> newStack = new LinkedListStack<>();
        // starting at the end of the String, push all elements to the stack
		for (int i=splitStrings.length-1; i>=0; i--) {
            newStack.push(splitStrings[i]);
        } // for
        return newStack;
    } // stringToStack

	/** 
     * Returns a String of the stack by calling the list's toString method 
     * @return String representation of the calling stack
     */
    public String toString() {
        return this.list.toString();
    } // toString

    /** Nested generic SinglyLinkedList class for stack implementation */
    private class SinglyLinkedList<E> {

        /** Nested generic Node class for list implementation */
		private class Node<E> {  
            
			private E element; 		// data the node holds
			private Node<E> next;	// reference to the next node
            
			/** Constructor method of the Node */
            public Node(E e, Node<E> n) {
                this.element = e;
                this.next = n;
            } // Node (constructor)

            /** 
             * Returns the element stored at the node. 
             * @return element of calling Node
             */
            public E getElement() { return element; } // getElement

            /** 
             * Returns the node that follows this one (or null if no such node). 
             * @return next Node of calling Node
             */
            public Node<E> getNext() { return next; } // getNext

            /** Sets the node's next reference to point to Node n. */
            public void setNext(Node<E> n) { next = n; } // setNext

        } // Node nested class

        private Node<E> head = null; 	// The head node of the list
        private int size = 0; 			// Number of nodes in the list

        /** Constructor method of the SinglyLinkedList */
        public SinglyLinkedList() { } // SinglyLinkedList (constructor)

        /** 
         * Returns the number of elements in the linked list. 
         * @return number of elements in calling list
         */
        public int size() { return this.size; } // size

        /** 
         * Tests whether the linked list is empty. 
         * @return boolean representing if the calling list is empty
         */
        public boolean isEmpty() { return (this.size == 0); } // isEmpty

        /** 
         * Returns (but does not remove) the first element of the list 
         * @return first element of list
         */
        public E first() {            
            return this.head.getElement();
        } // first

        /** Adds an element to the front of the list. */
        public void addFirst(E e) {     
            this.head = new Node<>(e, this.head); 
            this.size++;
        } // addFirst

        /** 
         * Removes and returns the first element of the list. 
         * @return element removed from the list
         */
        public E removeFirst() {        
            if (this.size != 0) {
                E removedElement = this.head.getElement();
                this.head = this.head.getNext();
                this.size--;
                return removedElement;
            } else {
                return null;
            } // if
        } // removeFirst

		/** 
         * Returns the list as a String of all the elements delimited by a space. 
         * @return String representation of the list
         */
        public String toString() {
            String output = "";
            if (!this.isEmpty()) {
                Node<E> n = this.head;
                for (int i=0; i<this.size-1; i++) {
                    output += n.getElement() + " ";
                    n = n.getNext();
                }
                output += n.getElement();
            }
            return output;
        } // toString

    } // SinglyLinkedList nested class

} // LinkedListStack