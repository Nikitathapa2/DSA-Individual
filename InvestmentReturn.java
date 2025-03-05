import java.util.PriorityQueue;

public class InvestmentReturn {

    // Function to find the Kth lowest combined investment return
    public static int findKthLowest(int[] returns1, int[] returns2, int k) {
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(a[0] * a[1], b[0] * b[1])
        );

        // Only push the first elements to limit unnecessary space usage
        for (int num1 : returns1) {
            minHeap.offer(new int[]{num1, returns2[0], 0}); // Store index to track pairs
        }

        int result = 0;
        while (k-- > 0 && !minHeap.isEmpty()) {
            int[] pair = minHeap.poll();
            result = pair[0] * pair[1];

            int nextIndex = pair[2] + 1;
            if (nextIndex < returns2.length) {
                minHeap.offer(new int[]{pair[0], returns2[nextIndex], nextIndex});
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Kth Lowest Combined Investment Returns:");

        // Example test cases
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        System.out.println("Test Case 1: returns1 = [2,5], returns2 = [3,4], k = 2 -> " +
                findKthLowest(returns1, returns2, 2)); // Output: 8

        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        System.out.println("Test Case 2: returns1 = [-4,-2,0,3], returns2 = [2,4], k = 6 -> " +
                findKthLowest(returns3, returns4, 6)); // Output: 0
    }
}
