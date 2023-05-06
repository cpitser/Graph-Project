package util;

/* Class used in a queue that holds a vertex, the path to vertex, and next node */
public class QueueNode {
    public VertexNode vertex;
    public Path path;
    public QueueNode next;
    
    public QueueNode(VertexNode vert, Path nodePath) { 
        vertex = vert; 
        path = nodePath;
    }
} // QueueNode