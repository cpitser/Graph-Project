package util;

public class QueueNode {
    public VertexNode vertex;
    public Path path;
    public QueueNode next;
    
    public QueueNode(VertexNode vert, Path nodePath) { 
        vertex = vert; 
        path = nodePath;
    }
} // QueueNode