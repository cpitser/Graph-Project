import java.util.Scanner;
import java.util.InputMismatchException;
import util.*;

/* Driver class of the graph, provides user interface and interactions */
public class GraphDriver {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please enter a single file to create a graph from as an argument.");
            System.exit(1);
        }
        Scanner input = new Scanner(System.in);
        Graph graph = new Graph(args[0]);
        System.out.println();
        graph.printVertices();
        // process input start vertex
        System.out.print("Select a start vertex: ");
        VertexNode start = graph.find(input.next());
        input.nextLine();
        if (start == null) { 
            System.out.println("Entered vertex does not exist."); 
            System.exit(1);    
        }
        // process input end index
        System.out.print("Select an end vertex: ");
        VertexNode end = graph.find(input.next());
        input.nextLine();
        if (end == null) { 
            System.out.println("Entered vertex does not exist."); 
            System.exit(1);    
        }
        if (start == end) { 
            System.out.println("Please enter two different vertices.");
            System.exit(1);
        }
        // list available commands for the user
        System.out.println("\nCommands: \na - find all paths\nl - find paths of given length");
        System.out.println("s - find shortest path\np - find paths that match given pattern");
        System.out.println("q - quit");
        // program loop, continue until quit
        while (true) {
            System.out.print("\nEnter a command: ");
            String command = input.next();
            input.nextLine();
            PathList paths;
            switch (command) {
                case "a": // user requests all paths
                    paths = graph.allPaths(start, end);
                    System.out.println("\nAll paths from " + start + " to " + end + ":");
                    paths.print();
                    break;
                case "l": // user requests paths of a cetain length
                    System.out.print("Enter the length of paths you would like to find: ");
                    try {
                        int length = input.nextInt();
                        paths = graph.lengthPaths(start,start,end,length);
                        System.out.println("\nAll paths from " + start + " to " + end + " with length " + length + ":");
                        paths.print();
                    } catch (InputMismatchException ime) {
                        System.out.println("Please enter a valid number.");
                    } finally {
                        input.nextLine();
                    }
                    break;
                case "s": // user requests shortest path
                    Path shortest = graph.shortestPath(start, end);
                    if (shortest == null) { System.out.println("No path exists."); }
                    else {
                        System.out.println("\nFirst shortest path from " + start + " to " + end + ":"); 
                        System.out.println("(Length: " + shortest.length + ")   " + shortest); 
                    }
                    break;
                case "p": // user requests paths of a certain pattern
                    // instructions for the user in pattern creation
                    System.out.println("*Instructions*: Use spaces between edge labels when used normally.");
                    System.out.println("When using regex operations, enclose edge labels (and dot operator) in parenthesis.");
                    System.out.println("Example: is_coworkers_with . (is_friends_with)* ((lives_with)|(is_classmates_with)) (.)*");
                    System.out.print("\nEnter the edge pattern of paths you would like to find: ");
                    String pattern = input.nextLine();
                    paths = graph.patternedPaths(start,end,pattern);
                    System.out.println("\nPaths that match this pattern:");
                    paths.print();
                    break;
                case "q": // user quits
                    System.exit(0);
                default: // other input (invalid)
                    System.out.println("Invalid command, try again.");
            }
        }
    }

} // GraphDriver