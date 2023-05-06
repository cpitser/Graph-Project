package util;

import java.util.NoSuchElementException;

/* 
 * Class used as a queue for vertices, used in breadth-first
 * search in the shortest path query 
 */
public class VertexQueue {

    private QueueNode front;
    private QueueNode end;

    public void enqueue(VertexNode vertex, Path path) {
        if (front == null) { 
            front = end = new QueueNode(vertex, path); 
        } else { 
            end.next = new QueueNode(vertex, path);
            end = end.next;
        }
    }

    public QueueNode dequeue() {
        if (front == null) {
            throw new NoSuchElementException();
        } else {
            QueueNode temp = front;
            front = front.next;
            return temp;
        }
    }

    public QueueNode front() { return front; }

    public boolean isEmpty() { return front==null; }

} // VertexNode