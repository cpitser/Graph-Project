import java.util.Scanner;
import java.util.InputMismatchException;

public class ListGraphDriver {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Please enter a single file to create a graph from.");
            System.exit(1);
        }
        Scanner input = new Scanner(System.in);
        ListGraph graph = new ListGraph(args[0]);
        System.out.println();
        graph.printVertices();
        // graph.printEdges();
        System.out.println();
        System.out.print("Select a start vertex: ");
        ListGraph.VertexNode start = graph.find(input.next());
        input.nextLine();
        if (start == null) { 
            System.out.println("Entered vertex does not exist."); 
            System.exit(1);    
        }
        System.out.print("Select an end vertex: ");
        ListGraph.VertexNode end = graph.find(input.next());
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
            ListGraph.PathList paths;
            switch (command) {
                case "a":
                    paths = graph.allPaths(start, end);
                    if (paths.isEmpty()) { System.out.println("No path exists."); }
                    else { 
                        while (paths.hasNext()) {
                            ListGraph.Path path = paths.next();
                            System.out.println(path.pathString + " length: " + path.length);
                        }
                    }
                    break;
                case "l":
                    System.out.print("Enter the length of paths you would like to find: ");
                    try {
                        int length = input.nextInt();
                        paths = graph.lengthPaths(start,start,end,length);
                        if (paths.isEmpty()) { System.out.println("No path exists."); }
                        else {
                            while (paths.hasNext()) {
                                ListGraph.Path path = paths.next();
                                System.out.println(path.pathString + " length: " + path.length);
                            }
                        }
                    } catch (InputMismatchException ime) {
                        System.out.println("Please enter a valid number.");
                    } finally {
                        input.nextLine();
                    }
                    break;
                case "s":
                    break;
                case "p":
                    break;
                case "q":
                    System.exit(0);
                default:
                    System.out.println("Invalid command, try again.");
            }
        }
    }

}