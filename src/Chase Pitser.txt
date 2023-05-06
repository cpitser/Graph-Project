Chase Pitser
cbp87422@uga.edu

Important compilation instructions:
I created a package of utility classes called util. All of these classes are required to be compiled.

Here are the compilation commands I used in order to compile the whole program:
javac -cp bin -d bin util\VertexNode.java util\EdgeNode.java
javac -cp bin -d bin util\Path.java util\PathList.java 
javac -cp bin -d bin util\QueueNode.java util\VertexQueue.java
javac -cp bin -d bin Graph.java
javac -cp bin -d bin GraphDriver.java

And I ran the program with the following command, replacing [input file] with an actual file path:
java -cp bin GraphDriver [input file]