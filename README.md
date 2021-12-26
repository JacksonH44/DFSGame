# DFSSearch
 A class assignment that finds a path in a maze. Only the classes Node.java, Edge.java, Graph.java, and Solver.java are mine, but they are 100% my code! The other files are exceptions or code that draws the maze once a path has been found.

The program takes in a file specifying the maze to be built. The maze includes paths that the player can go down freely, and some paths that require a bomb to break. There are different strengths of paths, corresponding to different strengths of bombs. There is a brick wall, a rock wall, and a metal wall. The brick wall takes 1 blast bomb to break, the rock wall takes 2 blast bombs to break, and the metal wall takes 1 melt bomb to break. 

The maze implements a DFS with backtracking, so the player will search as far down the maze in one direction first. It is important to note that the program just finds a path that exists, not the shortest path.
