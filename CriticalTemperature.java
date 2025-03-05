import java.util.PriorityQueue;

public class CriticalTemperature {
    // Function to determine the minimum tests needed to find the threshold temperature
    public static int calculateMinTests(int sensors, int maxAttempts) {
        int[][] attempts = new int[sensors + 1][maxAttempts + 1];

        for (int sensor = 1; sensor <= sensors; sensor++) {
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                attempts[sensor][attempt] = attempts[sensor - 1][attempt - 1] + attempts[sensor][attempt - 1] + 1;
                if (attempts[sensor][attempt] >= maxAttempts) {
                    return attempt;
                }
            }
        }
        return maxAttempts;
    }

    // Function to identify the Kth smallest product from two investment return lists
    public static int getKthSmallestProduct(int[] investmentA, int[] investmentB, int position) {
        PriorityQueue<int[]> heap = new PriorityQueue<>(
            (x, y) -> Integer.compare(investmentA[x[0]] * investmentB[x[1]], investmentA[y[0]] * investmentB[y[1]])
        );

        // Initializing heap with the first row of combinations
        for (int index = 0; index < investmentB.length; index++) {
            heap.offer(new int[]{0, index});
        }

        int result = 0;
        while (position-- > 0) {
            int[] selectedPair = heap.poll();
            int idxA = selectedPair[0], idxB = selectedPair[1];
            result = investmentA[idxA] * investmentB[idxB];

            // Adding next element from investmentA with the same index of investmentB
            if (idxA + 1 < investmentA.length) {
                heap.offer(new int[]{idxA + 1, idxB});
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // Running test cases for temperature threshold analysis
        System.out.println("Required Tests for Threshold Analysis:");
        System.out.println("Scenario 1: sensors=1, maxAttempts=2 -> " + calculateMinTests(1, 2));
        System.out.println("Scenario 2: sensors=2, maxAttempts=6 -> " + calculateMinTests(2, 6));
        System.out.println("Scenario 3: sensors=3, maxAttempts=14 -> " + calculateMinTests(3, 14));

        System.out.println("\nKth Smallest Investment Product:");
        // Running test cases for investment product analysis
        int[] investments1 = {2, 5};
        int[] investments2 = {3, 4};
        System.out.println("Case 1: investments1 = [2,5], investments2 = [3,4], position = 2 -> " +
                getKthSmallestProduct(investments1, investments2, 2));

        int[] investments3 = {-4, -2, 0, 3};
        int[] investments4 = {2, 4};
        System.out.println("Case 2: investments1 = [-4,-2,0,3], investments2 = [2,4], position = 6 -> " +
                getKthSmallestProduct(investments3, investments4, 6));
    }
}
