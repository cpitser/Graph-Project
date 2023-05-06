import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.regex.*;
import util.*;

/* Class used to represent a graph data structure */
public class Graph {
    private VertexNode[] vertices;
    private int V; // represents number of vertices in the list
    private int MAXSIZE = 10; // maximum size of the vertices array

    /* Constructor of the Graph, takes an input file for creation */
    public Graph(String filename) {
        V = 0;
        File file = new File(filename);
        initialize(file);
    }

    /* Private helper method used to initialize the vertices and edges of the graph */
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

    /* 
     * Private helper method used to add the infromation 
     * from a line from the file to the graph 
     */
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

    /* Private helper method used to check and resize the vertices array */
    private void resize() {
        if (V == MAXSIZE) {
            MAXSIZE *= 1.5;
            VertexNode[] temp = new VertexNode[MAXSIZE];
            for (int i=0; i<V; i++) { temp[i] = vertices[i]; }
            vertices = temp;
        }
    }

    /* Method used to list all vertices in the graph */
    public void printVertices() {
        System.out.print("Graph vertices: ");
        for (int i=0; i<V-1; i++) {
            System.out.print(vertices[i].label + ", "); 
        }
        System.out.println(vertices[V-1]);
    }

    /* 
     * Method used to find a vertex in the graph from an input String
     * returns null if not located 
     */
    public VertexNode find(String vertex) {
        for (int i=0; i<V; i++) { 
            if (vertices[i].label.equals(vertex)) {
                return vertices[i];
            }
        }
        return null;
    }

    /* Private helper method used to reset all vertices to their unvisited state */
    private void resetVisited() {
        for (int i=0; i<V; i++) { vertices[i].visited = false; }
    }

    /* Method used to return all paths from a source vertex to a sink vertex */
    public PathList allPaths(VertexNode source, VertexNode sink) {
        EdgeNode edge = source.edgeList;
        PathList pathList = new PathList();
        // if source is the sink the function is looking for
        if (source.equals(sink)) { 
            return new PathList(new Path(source.label, 0)); 
        }
        // goes through all of the edges from source
        while(edge != null) {
            // get the list of all of the subpaths to the destination from this source
            PathList subpaths = allPaths(edge.sink, sink);
            // go through all subpaths
            while (subpaths.hasNext()) {
                // add the paths with the current source and its subpaths to a list
                Path pathToAdd = subpaths.next();
                pathList.add(new Path(source + " --" + edge + "--> " + pathToAdd, pathToAdd.length+1));   
            }
            edge = edge.next;
        }
        // return the list of subpaths
        return pathList;
    }

    /* Method used to return all paths of a given length from source to sink */
    public PathList lengthPaths(VertexNode originalSource, VertexNode source, VertexNode sink, int length) {
        EdgeNode edge = source.edgeList;
        PathList pathList = new PathList();
        // if source is the sink the function is looking for
        if (source.equals(sink)) { 
            return new PathList(new Path(source.label, 0)); 
        }
        // goes through all of the edges from source
        while(edge != null) {            
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
            edge = edge.next;
        }
        // return the list of subpaths
        return pathList;
    }

    /* Method used to return the first shortest path from source to sink */
    public Path shortestPath(VertexNode source, VertexNode sink) {
        resetVisited();
        // use a queue to enact a breadth-first search algorithm to find shortest path
        VertexQueue q = new VertexQueue();
        q.enqueue(source, new Path(source.label, 0));
        // continue until no more vertices can be reached, queue is empty
        while (!q.isEmpty()) {
            QueueNode node = q.dequeue();
            EdgeNode edge = node.vertex.edgeList;
            while(edge != null) {           
                Path pathToVertex = new Path(node.path + " --" + edge + "--> " + edge.sink, node.path.length+1);
                // if destination, return this path
                if (edge.sink.equals(sink)) { 
                    return pathToVertex;
                }
                // if vertex not encountered (visited), enqueue it
                // no further checking for visited nodes required because 
                // this algorithm only enqueues non-encountered vertices
                if (!edge.sink.visited) {
                    edge.sink.visited = true;
                    q.enqueue(edge.sink, pathToVertex);
                }
                edge = edge.next;
            }
        }
        return null;
    }
    
    /* Method used to return all paths of a certain pattern from source to sink */
    public PathList patternedPaths(VertexNode source, VertexNode sink, String pattern) { 
        PathList patternPaths = new PathList();
        // edit the input path string to work properly with regex functions
        pattern = pattern + " ";
        pattern = pattern.replace(")* ", ")*");
        pattern = pattern.replace(")+ ", ")+");
        pattern = pattern.replace(")? ", ")?");
        pattern = pattern.replace(")*", " )*");
        pattern = pattern.replace(")+", " )+");
        pattern = pattern.replace(")?", " )?");
        pattern = pattern.replace(".","(\\w)*");
        PathList paths = allPaths(source, sink);
        Pattern p = Pattern.compile(pattern);
        // loop through all paths from source to sink
        while (paths.hasNext()) {
            Path current = paths.next();
            // create a new string of this path that can be matched with the pattern
            String[] pathArray = current.pathString.split(" ");
            String edgePathString = "";
            for (int i=0; i<pathArray.length; i++) {
                if (i%2==1) { edgePathString += pathArray[i].substring(2,pathArray[i].length()-3) + " "; }
            }
            // test if this path matches
            Matcher m = p.matcher(edgePathString);
            if (m.matches()) { 
                patternPaths.add(new Path(current)); 
            }
        }
        return patternPaths;
    }

} // Graph