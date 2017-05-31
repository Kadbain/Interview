package city_line;

/**
 * Created by Ps1X on 30.05.2017.
 */
public class Edge {
    private final Node from;
    private final Node to;

    private int weightNode;

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
