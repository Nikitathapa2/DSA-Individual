public class EmployeeReward {

    // Function to determine the minimum number of rewards needed
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        
        // Edge case: If there's only one employee, they get one reward
        if (n == 1) return 1;

        int[] rewards = new int[n];

        // Step 1: Initialize rewards with 1 for each employee
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }

        // Step 2: Left to right pass
        // Ensure that employees with higher ratings than their left neighbors get more rewards
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Step 3: Right to left pass
        // Ensure that employees with higher ratings than their right neighbors get more rewards
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Step 4: Sum up the total rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        return totalRewards;
    }

    public static void main(String[] args) {
        // Test cases to verify the solution
        System.out.println("Minimum Rewards Needed:");

        // Test Case 1
        int[] ratings1 = {1, 0, 2};
        System.out.println("Test Case 1: ratings = [1, 0, 2] -> " + minRewards(ratings1));  // Expected Output: 5

        // Test Case 2
        int[] ratings2 = {1, 2, 2};
        System.out.println("Test Case 2: ratings = [1, 2, 2] -> " + minRewards(ratings2));  // Expected Output: 4

        // Additional Test Case
        int[] ratings3 = {1, 3, 2, 2, 1};
        System.out.println("Test Case 3: ratings = [1, 3, 2, 2, 1] -> " + minRewards(ratings3));  // Expected Output: 7
    }
}
