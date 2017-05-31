package city_line;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * You are given a list of cities.
 * Each direct connection between two cities has its transportation cost (an integer bigger than 0).
 * The goal is to find the paths of minimum cost between pairs of cities.
 * Assume that the cost of each path
 * (which is the sum of costs of all direct connections belongning to this path) is at most 200000.
 * The name of a city is a string containing characters a,...,z and is at most 10 characters long.2)
 * <p>
 * Input
 * <p>
 * s [the number of tests <= 10]
 * n [the number of cities <= 10000]
 * NAME [city name]
 * p [the number of neighbours of city NAME]
 * nr cost [nr - index of a city connected to NAME (the index of the first city is 1)]
 * [cost - the transportation cost]
 * r [the number of paths to find <= 100]
 * NAME1 NAME2 [NAME1 - source, NAME2 - destination]
 * [empty line separating the tests]
 * <p>
 * Output
 * <p>
 * cost [the minimum transportation cost from city NAME1 to city NAME2 (one per line)]
 * <p>
 * Example
 * <p>
 * Input:
 * 1
 * 4
 * gdansk
 * 2
 * 2 1
 * 3 3
 * bydgoszcz
 * 3
 * 1 1
 * 3 1
 * 4 4
 * torun
 * 3
 * 1 3
 * 2 1
 * 4 1
 * warszawa
 * 2
 * 2 4
 * 3 1
 * 2
 * gdansk warszawa
 * bydgoszcz warszawa
 * <p>
 * Output:
 * 3
 * 2
 */
public class Main {
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    final Data data = new Data();
    private List<Data> graphs = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();

        try {

            main.addGraphs();
            main.results();
            main.printToConsole();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void readCity() throws IOException {
        System.out.println("Enter number of cities: ");
        int numberOfCity = Integer.parseInt(reader.readLine());

        for (int city = 0; city < numberOfCity; city++) {
            data.cities.add(new Node(city));
        }

        for (int city = 0; city < numberOfCity; city++) {
            System.out.println("Enter a name of city: ");
            data.cities.get(city).setName(reader.readLine());
            readEdges(city);
        }
    }

    private void readEdges(int fromCity) throws IOException {
        System.out.println("Enter a number of neighbours: ");
        int p = Integer.parseInt(reader.readLine());

        for (int i = 0; i < p; i++) {
            System.out.println("Enter a number of edges: ");
            int toCity = Integer.parseInt(reader.readLine());
            System.out.println("Enter a cost: ");
            int cost = Integer.parseInt(reader.readLine());

            Edge edge = new Edge(data.cities.get(fromCity), data.cities.get(toCity - 1), cost);
            data.lines.add(edge);
        }
    }

    private void readRutes() throws IOException {
        System.out.println("Enter the number of paths to find: ");
        int r = Integer.parseInt(reader.readLine());
        for (int i = 0; i < r; i++) {
            System.out.println("From city: ");
            String fromCity = reader.readLine();
            System.out.println("To city: ");
            String toCity = reader.readLine();
            Node cityFrom = null;
            Node cityTo = null;
            for (int city = 0; city < data.cities.size(); city++) {
                if (cityFrom == null && fromCity.equals(data.cities.get(city).getName())) {
                    cityFrom = data.cities.get(city);
                }
                if (cityTo == null && toCity.equals(data.cities.get(city).getName())) {
                    cityTo = data.cities.get(city);
                }
            }
            Edge edge = new Edge(cityFrom, cityTo, -1);
            data.rutes.put(i, edge);
        }

    }

    private Data read() throws IOException {
        readCity();
        readRutes();
        return data;
    }

    private void addGraphs() throws IOException {
        System.out.println("Enter number of graphs: ");
        int s = Integer.parseInt(reader.readLine());
        for (int i = 0; i < s; i++) {
            graphs.add(read());
        }

    }

    private void results() {

        for (Data graph : graphs) {
            Solution solution = new Solution(data.cities, data.lines);
            for (int i = 0; i < data.rutes.size(); i++) {
                solution.getShortestDistance(data.rutes.get(i));
                data.listPath.add(solution.getShortestPath(data.rutes.get(i)));
            }
        }
    }

    public void printToConsole() {
        for (Data graph : graphs) {
            System.out.println("------------------------------");
            for (int i = 0; i < data.rutes.size(); i++) {
                System.out.println(data.rutes.get(i).getWeightNode());
            }

//            System.out.println(data.cities);
//            System.out.println(data.lines);
//            System.out.println(data.rutes);
//            System.out.println(data.listPath);
        }
    }
}

class Data {
    List<Node> cities = new ArrayList<>();
    List<Edge> lines = new ArrayList<>();
    Map<Integer, Edge> rutes = new HashMap<>();
    List<List<Node>> listPath = new ArrayList<>();
}
class Solution {
    /**
     * A 2-dimensional matrix is an adjacency matrix. At each step in the
     * algorithm, D[i][j] is the shortest path from i to j using intermediate
     * vertices {1..k−1}. All weights of paths is initialized to
     * <code>initializeWeight(final Node[] nodes, final Edge[] edges) </code>
     * method.
     */
    private final int[][] distance;
    /**
     * A 2-dimensional matrix is an antecedence matrix. At each step in the
     * algorithm, P[i][j] is defined as the peak prior to the top of j on the
     * shortest path from vertex i to vertex j with intermediate vertices in the
     * set {1, 2, ..., k}
     */
    private final Node[][] p;
    /**
     * Create an instance of this class by describing the graph upon which it
     * will operate. <p> Note
     * <code>Node.id</code> must contain the index of the node in the
     * <code>nodes</code> parameter. Thus
     * <code>Node[1].id</code> must equal one. <p> On small computers the
     * practical maximum graph size with a 4-byte Node is about 10,000, at which
     * point the data size of an instance begins to exceed 1,5 GB.
     *
     * @param nodes array of Node; must be completely populated
     * @param edges array of Edge, completely populated; order is not important
     */
    public Solution(final Node[] nodes, final Edge[] edges) {
        final int maxNodes = 10_000;

        this.distance = initializeWeight(nodes, edges);
        this.p = new Node[nodes.length][nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes.length; j++) {
                for (int k = 0; k < nodes.length; k++) {
                    if (distance[j][i] != Integer.MAX_VALUE
                            && distance[i][k] != Integer.MAX_VALUE
                            && distance[j][i] + distance[i][k] < distance[j][k]) {
                        distance[j][k] = distance[j][i] + distance[i][k];
                        p[j][k] = nodes[i];
                    }
                }
            }
        }
    }

    public Solution(final List<Node> nodes, final List<Edge> edges) {
        this(nodes.toArray(new Node[0]), edges.toArray(new Edge[0]));
    }

    /**
     * Determines the length of the shortest path from vertex A (source) to
     * vertex B (target), calculated by summing the weights of the edges
     * traversed. <p> Note that distance, like path, is not commutative. That
     * is, distance(A,B) is not necessarily equal to distance(B,A).
     *
     * @param source Start Node
     * @param target End Node
     * @return The path length as the sum of the weights of the edges traversed,
     * or
     * <code>Integer.MAX_VALUE</code> if there is no path
     */
    public int getShortestDistance(final Node source, final Node target) {
        return distance[source.id][target.id];
    }

    public int getShortestDistance(final Edge root) {
        int d = getShortestDistance(root.getFrom(), root.getTo());
        root.setWeightNode(d);

        return d;
    }

    private int[][] initializeWeight(final Node[] nodes, final Edge[] edges) {
        int[][] weight = new int[nodes.length][edges.length];

        for (int i = 0; i < nodes.length; i++) {
            Arrays.fill(weight[i], Integer.MAX_VALUE);
        }

        for (Edge edge : edges) {
            weight[edge.getFrom().id][edge.getTo().id] = edge.getWeightNode();
        }

        return weight;
    }

    /**
     * This method constructs path from vertex
     * <code>source</code> to vertex
     * <code>target</code>.
     *
     * @param source the start vertex
     * @param target the end vertex
     * @return A List (ordered Collection) of Node, possibly empty
     */
    private List<Node> getIntermediatePath(final Node source, final Node target) {
        if (p[source.id][target.id] == null) {
            return new ArrayList<Node>();
        }

        final List<Node> path = new ArrayList<>();
        path.addAll(getIntermediatePath(source, p[source.id][target.id]));
        path.add(p[source.id][target.id]);
        path.addAll(getIntermediatePath(p[source.id][target.id], target));

        return path;
    }

    /**
     * Describes the shortest path from vertex A (source) to vertex B (target)
     * by returning a collection of the vertices traversed, in the order
     * traversed. If there is no such path an empty collection is returned. <p>
     * Note that because each Edge applies only to one direction of traverse,
     * the path from A to B may not be the same as the path from B to A.
     *
     * @param source the start vertex
     * @param target the end vertex
     * @return A List (ordered Collection) of Node, possibly empty
     */
    public List<Node> getShortestPath(final Node source, final Node target) {

        if (distance[source.id][target.id] == Integer.MAX_VALUE) {
            return new ArrayList<Node>();
        }
        final List<Node> path = getIntermediatePath(source, target);
        path.add(0, source);
        path.add(target);
        return path;
    }

    public List<Node> getShortestPath(final Edge rute) {
        return getShortestPath(rute.getFrom(), rute.getTo());
    }
}