package util;

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