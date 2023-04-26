package util;

import java.util.NoSuchElementException;

public class VertexQueue {

    private Node front;
    private Node end;

    public void enqueue(VertexNode vertex) {
        if (front == null) { 
            front = new Node(vertex); 
        } else { 
            end.next = new Node(vertex);
            end = end.next;
        }
    }

    public VertexNode dequeue() {
        if (front == null) {
            throw new NoSuchElementException();
        } else {
            VertexNode temp = front.vertex;
            front = front.next;
            return temp;
        }
    }

    public VertexNode front() { return front.vertex; }

    public boolean isEmpty() { return front==null; }

    private class Node {
        public VertexNode vertex;
        public Node next;
        public Node(VertexNode vert) { vertex = vert; }
    }

}