package util;

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