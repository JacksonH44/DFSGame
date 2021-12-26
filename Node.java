/**
 * A class that represents a node in a graph
 * @author jhowe48, Jackson Howe
 */

public class Node {
    /**
     * Instance variables: int name, boolean marked
     */
    private int name;
    private boolean marked;

    /**
     * Constructor class, creates a node from an int name
     * @param name
     */
    public Node (int name)
    {
        this.name = name;
        marked = false;
    }

    /**
     * Marks the node with the specified value
     * @param mark
     */
    public void setMark(boolean mark)
    {
        marked = mark;
    }

    /**
     * Returns the value with which the node has been marked
     * @return true if marked, false otherwise
     */
    public boolean getMark()
    {
        return marked;
    }

    /**
     * Returns the name of the node
     * @return name
     */
    public int getName()
    {
        return name;
    }

    /**
     * Compares two nodes for equality
     * @param otherNode
     * @return true if the two nodes have the same name, false otherwise
     */
    public boolean equals(Node otherNode)
    {
        return (this.name == otherNode.name);
    }
}
