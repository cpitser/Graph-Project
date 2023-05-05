import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.regex.*;
import util.*;

public class Graph {
    private VertexNode[] vertices;
    private int V;
    private int MAXSIZE = 10;

    public Graph(String filename) {
        V = 0;
        File file = new File(filename);
        initialize(file);
    }

    private void initialize(File file) {
        try {
            Scanner scanner = new Scanner(file);
            vertices = new VertexNode[MAXSIZE];
            while (scanner.hasNext()) {
                add(scanner.next(), scanner.next(), scanner.next());
                if (scanner.hasNextLine()) { scanner.nextLine(); }
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found or inaccessable.");
            System.exit(1);
        } catch (NoSuchElementException nsee) {
            System.out.println("Error within input file: expected more fields in a line.");
            System.exit(1);
        }
    }

    private void add(String source, String edge, String sink) {
        int sourceIndex = -1;
        int sinkIndex = -1;
        for (int i=0; i<V; i++) { 
            if (vertices[i].label.equals(source)) { sourceIndex = i; }
            if (vertices[i].label.equals(sink)) { sinkIndex = i; }
        }
        if (sourceIndex == -1) {
            resize();
            vertices[V] = new VertexNode(source);
            sourceIndex = V;
            V++;
        }
        if (sinkIndex == -1) {
            resize();
            vertices[V] = new VertexNode(sink);
            sinkIndex = V;
            V++;
        }
        EdgeNode newEdge = new EdgeNode(edge, vertices[sinkIndex]);
        newEdge.next = vertices[sourceIndex].edgeList;
        vertices[sourceIndex].edgeList = newEdge;
    }

    private void resize() {
        if (V == MAXSIZE) {
            MAXSIZE *= 1.5;
            VertexNode[] temp = new VertexNode[MAXSIZE];
            for (int i=0; i<V; i++) { temp[i] = vertices[i]; }
            vertices = temp;
        }
    }

    public void printVertices() {
        for (int i=0; i<V; i++) {
            System.out.print(vertices[i].label + " "); 
        }
    }

    public void printEdges() {
        for (int i=0; i<V; i++) {
            EdgeNode edge = vertices[i].edgeList;
            while (edge != null) {
                System.out.print(vertices[i].label + " --" + edge.label + "--> " + edge.sink.label);
                edge = edge.next;
            }
        }
    }

    public VertexNode find(String vertex) {
        for (int i=0; i<V; i++) { 
            if (vertices[i].label.equals(vertex)) {
                return vertices[i];
            }
        }
        return null;
    }

    private void resetVisited() {
        for (int i=0; i<V; i++) { vertices[i].visited = false; }
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
        pattern = pattern.replaceAll("\\.", "(\\\\w)*");
        pattern = pattern + " ";
        //pattern = pattern.replaceAll("\\s", "");
        PathList paths = allPaths(source, sink);
        Pattern p = Pattern.compile(pattern);
        System.out.println("Looking for: " + p.pattern());
        while (paths.hasNext()) {
            Path current = paths.next();
            String[] pathArray = current.pathString.split(" ");
            //System.out.println(current.pathString);
            String edgePathString = "";
            for (int i=0; i<pathArray.length; i++) {
                if (i%2==1) { edgePathString += pathArray[i].substring(2,pathArray[i].length()-3) + " "; }
            }
            Matcher m = p.matcher(edgePathString);
            if (m.matches()) { 
                System.out.println("Matching edge path:" + edgePathString);
                patternPaths.add(new Path(current)); 
            }
        }
        return patternPaths;
    }

} // Graph