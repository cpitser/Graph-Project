@echo off

javac -cp bin -d bin src\util\VertexNode.java src\util\EdgeNode.java 
javac -cp bin -d bin src\util\Path.java src\util\PathList.java src\util\VertexQueue.java src\util\LinkedListStack.java
javac -cp bin -d bin src\ListGraph.java
javac -cp bin -d bin src\ListGraphDriver.java