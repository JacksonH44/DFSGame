/**
 * A class that represents an edge of a graph
 * @author jhowe48, Jackson Howe
 */

public class Edge {
    /**
     * Instance variables: Node firstEndpoint, Node secondEndpoint, int type
     */
    private Node firstEndpoint;
    private Node secondEndpoint;
    private int type;

    /**
     * Class constructor
     * @param u
     * @param v
     * @param type
     */
    public Edge (Node u, Node v, int type)
    {
        firstEndpoint = u;
        secondEndpoint = v;
        this.type = type;
    }

    /**
     * Returns the first endpoitn of the edge
     * @return firstEndpoint
     */
    public Node firstEndpoint()
    {
        return firstEndpoint;
    }

    /**
     * Returns the second endpoint of the edge
     * @return secondEndpoint
     */
    public Node secondEndpoint()
    {
        return secondEndpoint;
    }

    // Returns the type of the edge
    /**
     * Returns the type of the edge
     * @return type
     */
    public int getType()
    {
        return type;
    }

    /**
     * Sets the type of edge to the specified value
     * @param type
     */
    public void setType(int type)
    {
        this.type = type;
    }

    /**
     * A method that tests for equality between two edges. Two edges are equal if they connect the same two nodes
     * @param otherEdge
     * @return true if this edge connects the same two nodes as the other edge; otherwise return false
     */
    public boolean equals(Edge otherEdge)
    {
        boolean cond1 = (this.firstEndpoint).equals(otherEdge.firstEndpoint) && (this.secondEndpoint).equals(otherEdge.secondEndpoint);
        boolean cond2 = (this.firstEndpoint).equals(otherEdge.secondEndpoint) && (this.secondEndpoint).equals(otherEdge.firstEndpoint);
        return cond1 || cond2;
    }
}
