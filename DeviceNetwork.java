import java.util.*;

public class DeviceNetwork {

    // Class to represent an edge in the graph
    static class Edge {
        int u, v, cost;

        public Edge(int u, int v, int cost) {
            this.u = u;
            this.v = v;
            this.cost = cost;
        }
    }

    // Disjoint Set Union (DSU) or Union-Find structure
    static class DSU {
        int[] parent, rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            Arrays.fill(rank, 1); // Initialize ranks to 1
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        // Find function with path compression
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // Path compression
            }
            return parent[x];
        }

        // Union by rank
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }

    // Function to find the minimum cost to connect all devices
    public static int minimumCost(int n, int[] modules, int[][] connections) {
        if (n == 1) return modules[0]; // Only one device, install its module.

        // Create a list of all edges (modules + connections)
        List<Edge> edges = new ArrayList<>();

        // Add edges representing module installation costs (connecting to a virtual node 'n')
        for (int i = 0; i < n; i++) {
            edges.add(new Edge(n, i, modules[i])); 
        }

        // Add actual wired connections
        for (int[] conn : connections) {
            edges.add(new Edge(conn[0] - 1, conn[1] - 1, conn[2])); // Convert to 0-based index
        }

        // Sort edges by cost (ascending order for Kruskal's algorithm)
        edges.sort(Comparator.comparingInt(e -> e.cost));

        // Initialize DSU
        DSU dsu = new DSU(n + 1); // +1 for virtual node
        int totalCost = 0, edgeCount = 0;

        // Apply Kruskal's algorithm
        for (Edge edge : edges) {
            if (dsu.find(edge.u) != dsu.find(edge.v)) { // If different sets, add to MST
                dsu.union(edge.u, edge.v);
                totalCost += edge.cost;
                edgeCount++;
            }

            // Stop if we've connected all devices
            if (edgeCount == n) {
                return totalCost;
            }
        }

        // If not all devices are connected, return -1 (impossible case)
        return -1;
    }

    public static void main(String[] args) {
        // Test case input
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {{1, 2, 1}, {2, 3, 1}};

        // Get the minimum total cost
        int result = minimumCost(n, modules, connections);

        // Output the result
        System.out.println("Minimum Total Cost to Connect All Devices: " + result);
    }
}
