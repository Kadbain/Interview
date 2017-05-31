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

    }

    private void readCity() throws IOException {
        int numberOfCity = Integer.parseInt(reader.readLine());

        for (int city = 0; city < numberOfCity; city++) {
            data.cities.add(new Node(city));
        }

        for (int city = 0; city < numberOfCity; city++) {
            data.cities.get(city).setName(reader.readLine());
            readEdges(city);
        }
    }

    private void readEdges(int fromCity) throws IOException {
        int p = Integer.parseInt(reader.readLine());

        for (int i = 0; i < p; i++) {
            int toCity = Integer.parseInt(reader.readLine());
            int cost = Integer.parseInt(reader.readLine());

            Edge edge = new Edge(data.cities.get(fromCity), data.cities.get(toCity - 1), cost);
            data.lines.add(edge);
        }
    }

    private void readRutes() throws IOException {
        int r = Integer.parseInt(reader.readLine());
        for (int i = 0; i < r; i++) {
            String fromCity = reader.readLine();
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
        int s = Integer.parseInt(reader.readLine());
        for (int i = 0; i < s; i++) {
            graphs.add(read());
        }

    }

    private void results() {
        for (Data graph : graphs) {

        }
    }
}

class Data {
    List<Node> cities = new ArrayList<>();
    List<Edge> lines = new ArrayList<>();
    Map<Integer, Edge> rutes = new HashMap<>();
}

class Solution {
    private final int[][] distance;
    private final Node[][] p;

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