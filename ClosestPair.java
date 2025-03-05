public class ClosestPair {

    // Function to find the lexicographically smallest pair of points with the smallest Manhattan distance
    public static int[] closestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int[] result = new int[2];
        int minDistance = Integer.MAX_VALUE;

        // Iterate over all pairs of points to find the closest pair
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance between points i and j
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Corrected condition to check the minimum distance and lexicographic order
                if (distance < minDistance || 
                    (distance == minDistance && (i < result[0] || (i == result[0] && j < result[1])))) {
                    
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // Example test case
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        // Get the lexicographically smallest pair of closest points
        int[] result = closestPair(x_coords, y_coords);

        // Output the result
        System.out.println("Closest pair of points indices: [" + result[0] + ", " + result[1] + "]");
    }
}
