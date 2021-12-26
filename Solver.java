/**
 * A class that creates and solves the maze
 * @author jhowe48, Jackson Howe
 */

import java.io.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Stack;

public class Solver {
    /**
     * Instance variables: int bBombs, int mBombs, Node entrance, Node exit, Graph grid, Stack<Node> path
     */
    private int bBombs;
    private int mBombs;
    private Node entrance;
    private Node exit;
    private Graph grid;
    private Stack<Node> path;

    /**
     * Constructor class, takes in an input file and creates the maze based off it
     * @param inputFile, the file with maze specifications
     * 
     * @throws LabyrinthException if the file doesn't exist or couldn't be closed
     */
    public Solver (String inputFile) throws LabyrinthException
    { 
        path = new Stack<Node>();
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

    /**
     * Returns a reference to the graph representing the labyrinthe
     * @return grid, the labyrinthe
     * 
     * @throws LabyrinthException if the grid doesn't exist
     */
    public Graph getGraph() throws LabyrinthException
    {
        if (grid == null)
        {
            throw new LabyrinthException("Oops! The graph does not exist!");
        }
        return grid;
    }

    /**
     * A private method called from solve to determine whether a path exists
     * @param start, the entrance node
     * @param end, the goal node
     * 
     * @return true if there is a path from start to end, false otherwise
     */
    private boolean pathExists(Node start, Node end)
    {
        start.setMark(true);  // Mark the node
        
        path.push(start);  // Add to the path

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

                for (int i = 0; i < neighbours.size(); i++)
                {
                    Edge newEdge = neighbours.get(i);  // Get the edge

                    // Set the node to discover
                    Node v = newEdge.firstEndpoint();
                    if (v.equals(u))
                    {
                        v = newEdge.secondEndpoint();
                    }

                    if (!(v.getMark()))  // If the node isn't marked
                    {
                        int edgeType = newEdge.getType();  // Check if we have the firepower to go this way

                        switch (edgeType)
                        {
                            case 1:  // Corridor
                                if (pathExists(v, end)) return true;
                                break;
                            case 2:  // Brick
                                if (bBombs >= 1)
                                {
                                    bBombs--;
                                    if (pathExists(v, end)) return true;
                                }
                                break;
                            case 3:  // Rock
                                if (bBombs >= 2)
                                {
                                    bBombs -= 2;
                                    if (pathExists(v, end)) return true;
                                }
                                break;
                            case 4:  // Metal
                                if (mBombs >= 1)
                                {
                                    mBombs--;
                                    if (pathExists(v, end)) return true;
                                }
                                break;
                        }
                    }
                }
                u.setMark(false);  // Unmark the node so we can explore paths in the future
                path.pop();  // Remove from path

                // If we remove a part of the path that involved using a bomb, replenish that bombs
                try
                {   
                    Node parent = path.peek();

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
                return false;
            }
            catch (GraphException e)
            {
                System.out.println("Node " + Integer.toString(u.getName()) + " does not exist");
            }
        }
        return false;
    }

    /**
     * Returns a Java iterator containing the nodes along the path from the entrance to exit of the labyrinthe, if such
     * a path exists
     * @return path.iterator()
     */
    public Iterator<Node> solve()
    {
        if (pathExists(entrance, exit)) return path.iterator();
        return null;
    }

}
