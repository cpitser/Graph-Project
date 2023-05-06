package util;

/* Class used to represent a list of paths, making operations easier */
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

    public void print() { 
        Path temp = current;
        current = first;
        if (this.isEmpty()) { System.out.println("No path exists."); }
        else { 
            int i = 0;
            while (this.hasNext()) {
                Path path = this.next();
                System.out.println("(Length: " + path.length + ")   " + path);
            }
        }
        current = temp;
    }
} // PathList