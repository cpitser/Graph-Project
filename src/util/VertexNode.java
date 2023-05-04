package util;

public class VertexNode {
    public final String label;
    public EdgeNode edgeList;
    public VertexNode next;
    public boolean visited;

    public VertexNode(String vertexLabel) {
        label = vertexLabel;
        visited = false;
    }

    public boolean equals(VertexNode other) {
        return (label.equals(other.label));
    }

    public String toString() { return label; }
} // VertexNode