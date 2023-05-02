@echo off

javac -cp bin -d bin src\util\VertexNode.java src\util\EdgeNode.java
javac -cp bin -d bin src\util\Path.java src\util\PathList.java 
javac -cp bin -d bin src\util\QueueNode.java src\util\VertexQueue.java
javac -cp bin -d bin src\Graph.java
javac -cp bin -d bin src\GraphDriver.java