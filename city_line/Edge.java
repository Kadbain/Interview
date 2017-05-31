package city_line;

/**
 * Created by Ps1X on 30.05.2017.
 */
public class Edge {
    private final Node from;
    private final Node to;

    private int weightNode;
    /**
     * Constructs an Edge object with the specified initial identifier
     * <code>from</code> and
     * <code>to</code> of the graph vertex, and
     * <code>weight</code> of the edge. The edge weight will not be equal to
     * infinity (
     * <code>Integer.MAX_VALUE</code>).
     *
     * @param from the graph vertex that starts from the edge.
     * @param to the graph vertex that ends from the edge.
     * @param weightNode the edge weight.
     */

    public Edge(final Node from, final Node to, final int weightNode) {
        this.from = from;
        this.to = to;
        this.weightNode = weightNode;
    }

    @Override
    public String toString() {
        return String.format("%s ---> %s, w=%d", getFrom(), getTo(), getWeightNode());
    }

    public int getWeightNode() {
        return weightNode;
    }

    public void setWeightNode(int weightNode) {
        this.weightNode = weightNode;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }
}
