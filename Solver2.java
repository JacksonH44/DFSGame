/* CS2210A */
/* Assignment 5 */
/* Jackson Howe */
/* 251149182 */
/* jhowe48@uwo.ca */
/* A class that represents the Labyrinth */

import java.io.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

public class Solver2 {
    // Instance variables: int bBombs, int mBombs, 
    //Node entrance, Node exit, Graph grid, Stack<Node> path
    private int bBombs;
    private int mBombs;
    private Node entrance;
    private Node exit;
    private Graph grid;
    private Stack<Node> path;
    private ArrayList<Node> markedNodes;

    // Constructor
    public Solver2 (String inputFile) throws LabyrinthException
    { 
        path = new Stack<Node>();

        markedNodes = new ArrayList<Node>();
        try
        {
            BufferedReader file = new BufferedReader (new FileReader (inputFile));  // Read in the file

            int size = Integer.parseInt(file.readLine());  // Labyrinth size
            int width = Integer.parseInt(file.readLine());  // Labyrinth width
            int length = Integer.parseInt(file.readLine()); // Labyrinth length
            bBombs = Integer.parseInt(file.readLine()); // Blast bombs
            mBombs = Integer.parseInt(file.readLine()); // Melt bombs

            grid = new Graph(width * length);  // Create the grid


            for (int i = 0; i < (2 * length) - 1; i++)  // Now we add edges
            {
                String currentLine = file.readLine();  // Define the current line of the file to make it more readable
                if (i % 2 == 0)  // Even number
                {
                    for (int j = 0; j < (2 * width) - 1; j++)  // Iterate through the symbols in the line
                    {
                        char symbol = currentLine.charAt(j);  // Get the current symbol
                        try 
                        {
                            // NOTE: all index line operations with char at are 
                            // well defined since every case where they are needed
                            // 0 < j < width - 1 and 0 < i < length - 1
                            switch (symbol)
                            {
                                case 'e': // Entrance
                                    entrance = grid.getNode((i/2)*width + j/2);  // Current node
                                    break;
                                case 'x': // Exit
                                    exit = grid.getNode((i/2)*width + j/2); // Current node
                                    break;
                                case '-': // Horizontal corridor
                                    Node u = grid.getNode((i/2)*width + j/2);  // Node left of edge
                                    Node v = grid.getNode((i/2)*width + j/2 + 1);  // Node right of edge
                                    grid.insertEdge(u, v, 1);
                                    break;
                                case 'b': // Horizontal brick wall
                                    Node u1 = grid.getNode((i/2)*width + j/2);  // Node left of edge
                                    Node v1 = grid.getNode((i/2)*width + j/2 + 1);  // Node right of edge
                                    grid.insertEdge(u1, v1, 2);
                                    break;
                                case 'r': // Horizontal rock wall
                                    Node u2 = grid.getNode((i/2)*width + j/2);  // Node left of edge
                                    Node v2 = grid.getNode((i/2)*width + j/2 + 1);  // Node right of edge
                                    grid.insertEdge(u2, v2, 3);
                                    break;
                                case 'm': // Horizontal metal wall
                                    Node u3 = grid.getNode((i/2)*width + j/2);  // Node left of edge
                                    Node v3 = grid.getNode((i/2)*width + j/2 + 1);  // Node right of edge
                                    grid.insertEdge(u3, v3, 4);
                                    break;
                            }
                        }
                        catch (GraphException e)
                        {
                            System.out.println("Oops! Something went wrong when creating the grid!");
                        }
                    }
                }
            
                else  // Odd number
                {
                    for (int j = 0; j < (2 * width) - 1; j++)  // Iterate through the symbols in the line
                    {
                        char symbol = currentLine.charAt(j);  // Get the current symbol

                        try 
                        {
                            // NOTE: all the line indexing operations are well defined 
                            // since 0 < j < width - 1 and 0 < i < length - 1

                            Node u = grid.getNode((i/2)*width + j/2);  // Get any potential nodes needed
                            Node v = grid.getNode((i/2 + 1)*width + j/2);  // to insert an edge

                            switch (symbol)
                            {
                                case '|': // Vertical corridor
                                    grid.insertEdge(u, v, 1);
                                    break;
                                case 'B': // Vertical brick wall
                                    grid.insertEdge(u, v, 2);
                                    break;
                                case 'R': // Vertical rock wall
                                    grid.insertEdge(u, v, 3);
                                    break;
                                case 'M': // Vertical metal wall
                                    grid.insertEdge(u, v, 4);
                                    break;
                            }
                        }
                        catch (GraphException e)
                        {
                            System.out.println("Oops! Something went wrong when creating the grid!");
                        }
                    }
                }
            }
            
            file.close();
        }
        catch (FileNotFoundException e)  // If there were issues opening the file
        {
            throw new LabyrinthException("The file " + inputFile + " was not found.");
        }
        catch (IOException e)  // If there were issues closing the file (if there are any, just in case)
        {
            throw new LabyrinthException("Oops! Something went wrong while closing the file " + inputFile);
        }
    }

    // Returns a reference to the graph representing the labyrinth
    public Graph getGraph() throws LabyrinthException
    {
        if (grid == null)
        {
            throw new LabyrinthException("Oops! The graph does not exist!");
        }
        return grid;
    }

    private void heuristic (ArrayList<Edge> edges)
    {
        int cost = exit.getName();
        for (int i = 0; i < edges.size() - 1; i++)
        {
            Edge e1 = edges.get(i);
            Edge e2 = edges.get(i + 1);

            int cost1 = (e1.firstEndpoint().getName() + e1.secondEndpoint().getName()) / 2;
            int cost2 = (e2.firstEndpoint().getName() + e2.secondEndpoint().getName()) / 2;
            if (cost1 > cost2)
            {
                edges.set(i + 1, e1);
                edges.set(i, e2);
            }
        }
    }

    // A private method called from solve to determine whether a path exists
    // and if it does, what that path is. We define a new method for added 
    // efficiency in recursively calling the algorithm
    private boolean pathExists(Node start, Node end)
    {
        start.setMark(true);  // Mark the node
        path.push(start);  // Add the node to the stack

        while (!path.isEmpty())
        {
            Node u = path.peek();
            

            if (u.equals(end))  // Found the path
            {
                return true;
            }

            try
            {
                ArrayList<Edge> neighbours = grid.incidentEdges(u);  // Return all edges incident on u
                heuristic(neighbours);

                for (int i = 0; i < neighbours.size(); i++)
                {
                    Edge newEdge = neighbours.get(i);  // Get the edge

                    // Set the node to discover
                    Node v = newEdge.firstEndpoint();
                    if (v.equals(u))
                    {
                        v = newEdge.secondEndpoint();
                    }

                    // Discovery edge that hasn't already been looked at
                    if (!(v.getMark()))
                    {
                        int edgeType = newEdge.getType();

                        switch (edgeType)
                        {
                            case 1:
                                return pathExists(v, end);
                            case 2:
                                if (bBombs >= 1)
                                {
                                    bBombs--;
                                    return pathExists(v, end);
                                }
                                break;
                            case 3:
                                if (bBombs >= 2)
                                {
                                    bBombs -= 2;
                                    return pathExists(v, end);
                                }
                                break;
                            case 4:
                                if (mBombs >= 1)
                                {
                                    mBombs--;
                                    return pathExists(v, end);
                                }
                                break;
                        }
                    }
                }

                path.pop();

                try
                {   Node parent = path.peek();

                    Edge backEdge = grid.getEdge(u, parent);

                 
                    switch (backEdge.getType())
                    {
                        case 2:
                            bBombs++;
                            break;
                        case 3:
                            bBombs += 2;
                            break;
                        case 4:
                            mBombs++;
                            break;
                    }
                }
                catch (EmptyStackException e)
                {
                    // Do nothing, this is the entrance
                }
            }
            catch (GraphException e)
            {
                System.out.println("Node " + Integer.toString(u.getName()) + " does not exist");
            }
        }
        return false;
    }

    // Returns a Java iterator containing the nodes along the path from the entrance to exit of 
    // the labyrinth, if such path exists
    public Iterator<Node> solve()
    {
        boolean found = pathExists(entrance, exit);
        if (found)
        {
            return path.iterator();
        }
        /*else
        {
            Node tmp = entrance;
            entrance = exit;
            exit = tmp;
        }
        boolean revFound = pathExists(entrance, exit);
        if (revFound) return path.iterator();*/
        return null;
    }

    public static void main(String[] args)
    {
        // Test 0
        try
        {
            Solver test1 = new Solver("lab1");
        }
        catch (LabyrinthException e)
        {
            System.out.println("Oops! error on construction!");
        }

        // Test 1
        try
        {
            Solver s0 = new Solver("lab1");
            Graph g0 = s0.getGraph();
        }
        catch (LabyrinthException e)
        {
            System.out.println("Test 1 failed");
        }
        System.out.println("Test 1 passed");

        // Test 2
        try
        {
            Solver s1 = new Solver("lab1");
            Graph g1 = s1.getGraph();
            try
            {
                Node u = g1.getNode(20);
            }
            catch (GraphException e)
            {
                System.out.println("Test 2 passed");
            }
        }   
        catch (LabyrinthException e)
        {
            System.out.println("Test 2 failed");
        }

        // Test 3
        try
        {
            Solver2 s2 = new Solver2("lab1");
            Graph g2 = s2.getGraph();
            try
            {
                Node u = g2.getNode(0);
                Node v = g2.getNode(13);
                if (u.equals(s2.entrance) && v.equals(s2.exit))
                {
                    System.out.println("Test 3 passed");
                }
                else
                {
                    System.out.println("Test 3 failed");
                }
            }
            catch (GraphException e)
            {
                System.out.println("Test 3 failed");
            }
        }   
        catch (LabyrinthException e)
        {
            System.out.println("Test 3 failed");
        }

        // Test 4
        try
        {
            Solver s2 = new Solver("lab1");
            Graph g2 = s2.getGraph();
            try
            {
                Node u = g2.getNode(9);
                Node v = g2.getNode(14);
                Node x = g2.getNode(2);
                Node y = g2.getNode(7);
            
                Edge uv = g2.getEdge(u, v);
                Edge xy = g2.getEdge(x, y);

                if (uv.getType() == 4 && xy.getType() == 3)
                {
                    System.out.println("Test 4 passed");
                }
                else
                {
                    System.out.println("Test 4 failed");
                }
            }
            catch (GraphException e)
            {
                System.out.println("Test 4 failed");
            }
        }   
        catch (LabyrinthException e)
        {
            System.out.println("Test 4 failed");
        }

        try
        {
            Solver2 s1 = new Solver2("lab5");
            Iterator<Node> finish = s1.solve();
        }
        catch (LabyrinthException e)
        {
            e.printStackTrace();
        }

    }
}
