package city_line;

/**
 * Created by Ps1X on 30.05.2017.
 */
public class Node {
    final int id;
    private String name;
    /**
     * Constructs a Node object with the specified initial identifier
     * <code>id</code> and <code>name</code> of the graph vertex.
     *
     * @param id number of the graph vertex
     * @param name the string name of the graph vertices
     */
    public Node(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Node(final int id) {
        this(id, null);
    }

    @Override
    public String toString() {
        return String.format("id = %d, name = %s", id, name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
