package util;

public class Path {
    public String pathString;
    public int length;
    public Path next;

    public Path(Path old) { 
        pathString = old.pathString; 
        length = old.length;    
    }

    public Path(String path, int len) {
        pathString = path;
        length = len;
        next = null;
    }

    public String toString() { return pathString; }
} // Path