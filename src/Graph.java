import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.regex.*;
import util.*;

public class Graph {
    private VertexNode vertices;
    private int V;
    private Path memoization;

    public Graph(String filename) {
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
            System.out.println("File not found or inaccessable.");
            System.exit(1);
        } catch (NoSuchElementException nsee) {
            System.out.println("Error within input file: expected more fields in a line.");
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
                    System.out.println("Error within input file (edge creation).");
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
            System.out.println("File not found or inaccessable.");
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
                System.out.println(current.label + " --" + edge.label + "--> " + edge.sink.label);
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
                    pathList.add(new Path(source + " --" + edge + "--> " + pathToAdd, pathToAdd.length+1));   
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
                if (source.equals(originalSource)) { checkLength = true; }
                while (subpaths.hasNext()) {
                    Path pathToAdd = subpaths.next();
                    // add the paths with the current source and its subpaths to a list
                    if (checkLength) {
                        if (pathToAdd.length+1 == length) {
                            pathList.add(new Path(source + " --" + edge + "--> " + pathToAdd, pathToAdd.length+1));
                        }   
                    } else {
                        pathList.add(new Path(source + " --" + edge + "--> " + pathToAdd, pathToAdd.length+1));
                    }
                }
            }
            edge = edge.next;
        }
        // return the list of subpaths
        return pathList;
    }

    // Find shortest path(s) with minimum number of edges
    public Path shortestPath(VertexNode source, VertexNode sink) {
        resetVisited();
        VertexQueue q = new VertexQueue();
        q.enqueue(source, new Path(source.label, 0));
        while (!q.isEmpty()) {
            QueueNode node = q.dequeue();
            EdgeNode edge = node.vertex.edgeList;
            while(edge != null) {           
                Path pathToVertex = new Path(node.path + " --" + edge + "--> " + edge.sink, node.path.length+1);
                // if this edge is not visited
                if (edge.sink.equals(sink)) { 
                    return pathToVertex;
                }
                if (!edge.sink.visited) {
                    edge.sink.visited = true;
                    q.enqueue(edge.sink, pathToVertex);
                }
                edge = edge.next;
            }
        }
        return null;
    }
    
    // Find paths that match a pattern between A and B
    public PathList patternedPaths(VertexNode source, VertexNode sink, String pattern) { 
        PathList patternPaths = new PathList();
        pattern = pattern.replaceAll("\\.", ".*");
        //pattern = pattern.replaceAll("\\s", "");
        PathList paths = allPaths(source, sink);
        Pattern p = Pattern.compile(pattern);
        //System.out.println("Looking for: " + p.pattern());
        while (paths.hasNext()) {
            Path current = paths.next();
            String[] pathArray = current.pathString.split(" ");
            //System.out.println(current.pathString);
            String edgePathString = "";
            for (int i=0; i<pathArray.length; i++) {
                if (i%2==1) { edgePathString += pathArray[i].substring(2,pathArray[i].length()-3) + " "; }
            }
            Matcher m = p.matcher(edgePathString.trim());
            if (m.matches()) { 
                System.out.println("Matching edge path:" + edgePathString);
                patternPaths.add(new Path(current)); 
            }
        }
        return patternPaths;
    }

} // Graph