package util;

/* Class used to represent an edge in the graph */
public class EdgeNode {
    public final VertexNode sink;
    public final String label;
    public EdgeNode next;

    public EdgeNode(String edgeLabel, VertexNode dest) {
        sink = dest;
        label = edgeLabel;
    }

    public boolean equals(EdgeNode other) { return (label.equals(other.label) && sink.label.equals(other.sink.label)); }

    public String toString(){ return label; }
} // EdgeNode