/**
 * A class that represents an undirected graph
 * @author jhowe48, Jackson Howe
 */

import java.util.ArrayList;
import java.lang.Math;

public class Graph implements GraphADT{
    /**
     * Instance variable: ArrayList<ArrayList<Edge>> edges, ArrayList<Node> vertices
     */
    private ArrayList<ArrayList<Edge>> edges;
    private ArrayList<Node> vertices;

    /**
     * Class constructor. Takes in a value n and creates n nodes of the graph numbered 0, 1, ..., n -1
     * @param n
     */
    public Graph (int n)
    {
        vertices = new ArrayList<Node>(n);  // Create new vertices list

        // Initialize the vertices list
        for (int i = 0; i < n; i++)
        {
            Node newNode = new Node(i);
            vertices.add(newNode);
        }

        edges = new ArrayList<ArrayList<Edge>>(n);  // Create new edge list

        // Initialize the edge list
        for (int i = 0; i < n; i++)
        {
            edges.add(new ArrayList<Edge>());
        }
    }

    /**
     * Adds to the graph an edge connecting nodes u and v
     * @param u, first endpoint
     * @param v, second endpoint
     * @param type, edge type
     * 
     * @throws GraphException if the nodes do not exist in the graph
     */
    public void insertEdge (Node u, Node v, int type) throws GraphException
    {
        // Verify the nodes are in the graph
        if (u.getName() >= vertices.size() || v.getName() >= vertices.size())
        {
            throw new GraphException("Node " + Integer.toString(u.getName()) + " does not exist.");
        }

        Edge newEdge = new Edge(u, v, type);  // Create a new edge

        // Get the two edge lists we will be inserting into
        ArrayList<Edge> refEdge1 = edges.get(u.getName());
        ArrayList<Edge> refEdge2 = edges.get(v.getName());

        for (Edge edge : refEdge1)
        {
            // Determine if the edge is already in the edge list
            // NOTE: since we are using an undirected graph, we only need to check one row of the adjacency list
            // since both refEdge1 and refEdge2 hold the same edges.
            if (edge.firstEndpoint().getName() == u.getName() && edge.secondEndpoint().getName() == v.getName())
            {
                throw new GraphException("Node " + Integer.toString(u.getName()) + " does not exist.");
            }
            else if (edge.firstEndpoint().getName() == v.getName() && edge.secondEndpoint().getName() == u.getName())
            {
                throw new GraphException("Node " + Integer.toString(u.getName()) + " does not exist.");
            }
        }

        // Add in the edges
        refEdge1.add(newEdge);
        refEdge2.add(newEdge);
    }

    // Returns the node with the specified name
    /**
     * Returns the node with the specified name
     * @param name, the name of the node 
     * 
     * @return vertices.get(name), the node corresponding to name
     * 
     * @throws GraphException if the nodes do not exist in the graph
     */
    public Node getNode (int name) throws GraphException
    {
        try
        {
            return vertices.get(name);
        }
        catch (IndexOutOfBoundsException e)
        {
            throw new GraphException("Node " + Integer.toString(name) + " does not exist");
        }
    }

    /**
     * Returns a list storing all edges incident on node u
     * @param u, the node in question
     * 
     * @return res, the list of all incident edges
     * 
     * @throws GraphException if the node does not exist within the graph
     */
    public ArrayList<Edge> incidentEdges(Node u) throws GraphException
    {
        // Verify the node is in the graph
        if (u.getName() >= vertices.size()) 
        {
            throw new GraphException("Node " + Integer.toString(u.getName())
            + " does not exist.");
        }
        ArrayList<Edge> res = edges.get(u.getName());  // Get the edge list corresponding to that node

        if (res.size() == 0)  // There are no nodes in the edge list
        {
            return null;
        }

        return res;  // Return the edge list
    }

    /**
     * Returns the edge connectin nodes u and v
     * @param u, first node
     * @param v, second node
     * 
     * @return tmpEdge, the edge connecting nodes u and v
     * 
     * @throws GraphException if either of the nodes does not exist within the graph, 
     * or there is no edge between the two nodes
     */
    public Edge getEdge (Node u, Node v) throws GraphException
    {
        // Verify the nodes are both in the graph
        if (u.getName() >= vertices.size() || v.getName() >= vertices.size()) 
        {
            throw new GraphException("Node " + Integer.toString(Math.min(u.getName(), v.getName()))
            + " does not exist.");
        }

        int minLength = Math.min(u.getName(), v.getName());  // Get the minimum of the two nodes
        ArrayList<Edge> res = edges.get(minLength);  // Get the edge list corresponding the the minimum

        for (Edge tmpEdge : res)
        {
            // Since this is an undirected graph we check both directions
            if ((tmpEdge.firstEndpoint()).equals(u) && (tmpEdge.secondEndpoint()).equals(v))
            {
                return tmpEdge;  // Edge was found
            }
            if ((tmpEdge.firstEndpoint()).equals(v) && (tmpEdge.secondEndpoint()).equals(u))
            {
                return tmpEdge;  // Edge was found
            }
        }
        // No edge was found
        throw new GraphException("Edge between nodes " + Integer.toString(u.getName()) + " and " + 
        Integer.toString(u.getName()));  
    }
 
    /**
     * Returns true if and only if the nodes u and v are adjacent
     * @param u, first node
     * @param v, second node
     * 
     * @return true iff the nodes u and v are adjacent; otherwise false
     * 
     * @throws GraphException if either u or v isn't in the graph
     */
    public boolean areAdjacent(Node u, Node v) throws GraphException
    {
        // Verify the nodes are both in the graph
        if (u.getName() >= vertices.size() || v.getName() >= vertices.size()) 
        {
            throw new GraphException("Node " + Integer.toString(Math.min(u.getName(), v.getName()))
            + " does not exist.");
        }  

        int minLength = Math.min(u.getName(), v.getName());  // Get the minimum of the two nodes
        ArrayList<Edge> res = edges.get(minLength);  // Get the edge list corresponding the the minimum

        for (Edge tmpEdge : res)
        {
            // Since this is an undirected graph we check both directions
            if ((tmpEdge.firstEndpoint()).equals(u) && (tmpEdge.secondEndpoint()).equals(v))
            {
                return true; // An edge exists, so the nodes are adjacent
            }
            if ((tmpEdge.firstEndpoint()).equals(v) && (tmpEdge.secondEndpoint()).equals(u))
            {
                return true; // An edge exists, so the nodes are adajcent
            }
        }
        return false;  // No edge was found
    }
}
