package util;

public class EdgeNode {
    public final VertexNode sink;
    public final String label;
    public EdgeNode next;

    public EdgeNode(String edgeLabel, VertexNode dest) {
        sink = dest;
        label = edgeLabel;
    }

    public boolean equals(EdgeNode other) {
        return (label.equals(other.label) && sink.label.equals(other.sink.label));
    }
} // EdgeNode