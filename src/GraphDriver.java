import java.util.Scanner;
import java.util.InputMismatchException;
import util.*;

public class GraphDriver {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please enter a single file to create a graph from.");
            System.exit(1);
        }
        Scanner input = new Scanner(System.in);
        Graph graph = new Graph(args[0]);
        System.out.println();
        graph.printVertices();
        // graph.printEdges();
        System.out.println();
        System.out.print("Select a start vertex: ");
        VertexNode start = graph.find(input.next());
        input.nextLine();
        if (start == null) { 
            System.out.println("Entered vertex does not exist."); 
            System.exit(1);    
        }
        System.out.print("Select an end vertex: ");
        VertexNode end = graph.find(input.next());
        input.nextLine();
        if (end == null) { 
            System.out.println("Entered vertex does not exist."); 
            System.exit(1);    
        }
        System.out.println("\nCommands: \na - find all paths\nl - find paths of given length");
        System.out.println("s - find shortest path\np - find paths that match given pattern");
        System.out.println("q - quit");
        // program loop
        while (true) {
            System.out.print("\nEnter a command: ");
            String command = input.next();
            input.nextLine();
            PathList paths;
            switch (command) {
                case "a":
                    paths = graph.allPaths(start, end);
                    System.out.println("All paths from " + start + " to " + end + ":");
                    paths.print();
                    break;
                case "l":
                    System.out.print("Enter the length of paths you would like to find: ");
                    try {
                        int length = input.nextInt();
                        paths = graph.lengthPaths(start,start,end,length);
                        System.out.println("All paths from " + start + " to " + end + " with length " + length + ":");
                        paths.print();
                    } catch (InputMismatchException ime) {
                        System.out.println("Please enter a valid number.");
                    } finally {
                        input.nextLine();
                    }
                    break;
                case "s":
                    Path shortest = graph.shortestPath(start, end);
                    if (shortest == null) { System.out.println("No path exists."); }
                    else {
                        System.out.println("First shortest path from " + start + " to " + end + ":"); 
                        System.out.println("(Length: " + shortest.length + ")   " + shortest); 
                    }
                    break;
                case "p":
                    System.out.println("When entering pattern, add spaces between each edge label.");
                    System.out.println("Instead of .* use (.)* and do not use this ");
                    System.out.print("Enter the pattern of edges of paths you would like to find: ");
                    String pattern = input.nextLine();
                    paths = graph.patternedPaths(start,end,pattern);
                    System.out.println("Printing paths:");
                    paths.print();
                    System.out.println("Done printing paths.");
                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("Invalid command, try again.");
            }
        }
    }

} // GraphDriver