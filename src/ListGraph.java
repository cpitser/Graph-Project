import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

public class ListGraph {
    private VertexNode vertices;
    private int V;
    private Path memoization;

    public ListGraph(String filename) {
        V = 0;
        File file = new File(filename);
        initializeVertices(file);
        initializeEdges(file);
    }

    private void initializeVertices(File file) {
        try {
            Scanner vertexScanner = new Scanner(file);
            vertices = new VertexNode(vertexScanner.next());
            vertexScanner.next();
            vertices.next = new VertexNode(vertexScanner.next());
            vertexScanner.nextLine();
            while(vertexScanner.hasNext()) {
                VertexNode current = vertices;
                String newLabel1 = vertexScanner.next();
                vertexScanner.next();
                String newLabel2 = vertexScanner.next();
                boolean firstExists = false;
                boolean secondExists = false;
                if (vertexScanner.hasNextLine()) { vertexScanner.nextLine(); }
                while (current != null) {
                    if (current.label.equals(newLabel1)) {
                        firstExists = true;
                    } 
                    if (current.label.equals(newLabel2)) {
                        secondExists = true;
                    } 
                    if (current.next == null) {
                        if (!firstExists) { 
                            current.next = new VertexNode(newLabel1); 
                            current = current.next;
                            V++;
                        }
                        if (!secondExists) { 
                            current.next = new VertexNode(newLabel2);                            
                            V++;
                        }
                        break;
                    } 
                    else { current = current.next; }
                }
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("File not found or inaccessable.");
            System.exit(1);
        } catch (NoSuchElementException nsee) {
            System.err.println("Error within input file: expected more fields in a line.");
            System.exit(1);
        }
    }
    
    private void initializeEdges(File file) {
        try { 
            Scanner edgeScanner = new Scanner(file);
            int line = 1;
            while (edgeScanner.hasNext()) {
                VertexNode vertex = vertices;
                VertexNode source = null;
                VertexNode sink = null;
                String sourceLabel = edgeScanner.next();
                String edgeLabel = edgeScanner.next();
                String sinkLabel = edgeScanner.next();
                if (edgeScanner.hasNextLine()) { edgeScanner.nextLine(); }
                // find source and sink vertices in which the edge starts and ends
                while (vertex != null) {
                    if (vertex.label.equals(sourceLabel)) { source = vertex; }
                    if (vertex.label.equals(sinkLabel)) { sink = vertex; }
                    vertex = vertex.next;
                }
                // if source or sink null or same, error
                if (source == null || sink == null || sourceLabel.equals(sinkLabel)) {
                    System.err.println("Error within input file (edge creation).");
                    System.exit(1);
                }
                // first edge for this source vertex
                if (source.edgeList == null) {
                    source.edgeList = new EdgeNode(edgeLabel, sink);
                } 
                // not first edge for this source vertex
                else {
                    EdgeNode edge = source.edgeList;
                    EdgeNode newEdge = new EdgeNode(edgeLabel, sink);
                    while (edge != null) { 
                        if (edge.equals(newEdge)) { break; }
                        if (edge.next == null) { 
                            edge.next = newEdge;
                            //System.out.println("New Edge: " + sourceLabel + " --" + edgeLabel + "--> " + sinkLabel); DEBUGGING
                            break; 
                        }
                        edge = edge.next;
                    }
                }
                line++;
            }
        } catch (FileNotFoundException fnfe) {
            System.err.println("File not found or inaccessable.");
            System.exit(1);
        }
    }

    public void printVertices() {
        VertexNode current = vertices;
        System.out.print("Vertices: ");
        while (current.next != null) {
            System.out.print(current.label + ", ");
            current = current.next;
        }
        System.out.println(current.label);
    }

    public void printEdges() {
        VertexNode current = vertices;
        while (current != null) {
            EdgeNode edge = current.edgeList;
            while (edge != null) {
                System.out.println(current.label + " -- " + edge.label + " --> " + edge.sink.label);
                edge = edge.next;
            }
            current = current.next;
        }
    }

    public VertexNode find(String vertex) {
        VertexNode current = vertices;
        boolean found = false;
        while (current != null) { 
            if (current.label.equals(vertex)) { return current; } 
            current = current.next;    
        }
        return null; 
    }

    private void resetVisited() {
        VertexNode current = vertices;
        while (current != null) {
            current.visited = false;
            current = current.next;
        }
    }

    // Find all directed paths between A and B
        // recursively go through all unvisited nodes until reached sink or no more unvisited nodes
        // add each path to a linked list
    public PathList allPaths(VertexNode source, VertexNode sink) {
        resetVisited();
        EdgeNode edge = source.edgeList;
        PathList pathList = new PathList();
        // if source is the sink the function is looking for
        if (source.equals(sink)) { 
            return new PathList(new Path(source.label, 0)); 
        }
        // goes through all of the edges from source
        while(edge != null) {
            // if this edge is not visited
            if (!edge.sink.visited) {
                edge.sink.visited = true; 
                // get the list of all of the subpaths to the destination from this source
                PathList subpaths = allPaths(edge.sink, sink);
                // go through all subpaths
                while (subpaths.hasNext()) {
                    // add the paths with the current source and its subpaths to a list
                    Path pathToAdd = subpaths.next();
                    pathList.add(new Path(source.label + " -- " + edge.label + " --> " + pathToAdd.pathString, pathToAdd.length+1));   
                }
            }
            edge = edge.next;
        }
        // return the list of subpaths
        return pathList;
    }

    // Find directed paths of a given length (edge count) between A and B
    public PathList lengthPaths(VertexNode originalSource, VertexNode source, VertexNode sink, int length) {
        resetVisited();
        EdgeNode edge = source.edgeList;
        PathList pathList = new PathList();
        // if source is the sink the function is looking for
        if (source.equals(sink)) { 
            return new PathList(new Path(source.label, 0)); 
        }
        // goes through all of the edges from source
        while(edge != null) {            
            // if this edge is not visited
            if (!edge.sink.visited) {
                edge.sink.visited = true; 
                // get the list of all of the subpaths to the destination from this source
                PathList subpaths = lengthPaths(originalSource, edge.sink, sink, length);
                // go through all subpaths
                boolean checkLength = false;
                if (source.label.equals(originalSource.label)) { checkLength = true; }
                while (subpaths.hasNext()) {
                    Path pathToAdd = subpaths.next();
                    // add the paths with the current source and its subpaths to a list
                    if (checkLength) {
                        if (pathToAdd.length+1 == length) {
                            pathList.add(new Path(source.label + " -- " + edge.label + " --> " + pathToAdd.pathString, pathToAdd.length+1));
                        }   
                    } else {
                        pathList.add(new Path(source.label + " -- " + edge.label + " --> " + pathToAdd.pathString, pathToAdd.length+1));
                    }
                }
            }
            edge = edge.next;
        }
        // return the list of subpaths
        return pathList;
    }

    /*public PathList shortestPath(VertexNode source, VertexNode sink) {}*/

    // Find shortest path(s) with minimum number of edges
    
    // Find paths that match a pattern between A and B
        /* 
        ( ) = grouping
        | = or
        * = previous edge label occurs 0 or more times
        + = previous edge label occurs 1 or more times
        ? = previous edge label occurs 0 or 1 time
        . = any single edge label
        R = a+(d|b)ab matches adab, aadab, abab etc.
        R = a(bc)* matches abc, a, abcbc etc.
        R = a.c matches abc, adc, etc.
        R = ab? matches a, ab
        */
    
    ////               ////
    //  nested classes  //
    ////              ////

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
    } // VertexNode

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
    
    public class Path {
        public final String pathString;
        public final int length;
        public Path next;

        public Path(String path, int len) {
            pathString = path;
            length = len;
            next = null;
        }
    } // Path

    public class PathList {
        private Path first;
        private Path last;
        public Path current;

        public PathList() {}

        public PathList(Path path) { first  = last = current = path; }

        public void add(Path path) {
            if (first == null) { first = last = current = path; } 
            else { 
                last.next = path;
                last = last.next; 
            }
        }

        public boolean hasNext() { return current != null; }

        public Path next() {
            Path temp = current;
            current = current.next;
            return temp;
        }

        public boolean isEmpty() { return first==null; }
    }

} // ListGraph