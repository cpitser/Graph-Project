package util;

/* Class used to represet a vertex in the graph */
public class VertexNode {
    public final String label;
    public EdgeNode edgeList;
    public boolean visited;

    public VertexNode(String vertexLabel) { label = vertexLabel; }

    public boolean equals(VertexNode other) { return (label.equals(other.label)); }

    public String toString() { return label; }
} // VertexNode