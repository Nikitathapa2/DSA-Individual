import java.util.*;

public class HashtagCounter {
    public static class HashtagCount implements Comparable<HashtagCount> {
        String hashtag;
        int count;

        public HashtagCount(String hashtag, int count) {
            this.hashtag = hashtag;
            this.count = count;
        }

        @Override
        public int compareTo(HashtagCount other) {
            // Sort by count descending, then by hashtag alphabetically
            if (this.count != other.count) {
                return Integer.compare(other.count, this.count); // Descending order
            }
            return this.hashtag.compareTo(other.hashtag); // Alphabetical order
        }
    }

    public static List<HashtagCount> countHashtags(List<String> tweets) {
        Map<String, Integer> hashtagCounts = new HashMap<>();

        for (String tweet : tweets) {
            // Extract words from the tweet
            String[] words = tweet.split("\\s+");

            for (String word : words) {
                if (word.startsWith("#") && word.length() > 1) {
                    // Normalize the hashtag (remove punctuation, convert to lowercase)
                    String hashtag = word.replaceAll("[^a-zA-Z0-9#]", "").toLowerCase();
                    hashtagCounts.put(hashtag, hashtagCounts.getOrDefault(hashtag, 0) + 1);
                }
            }
        }

        List<HashtagCount> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : hashtagCounts.entrySet()) {
            result.add(new HashtagCount(entry.getKey(), entry.getValue()));
        }

        // Sort hashtags based on count and then alphabetically
        Collections.sort(result);
        return result;
    }

    public static void printHashtagTable(List<HashtagCount> hashtags) {
        System.out.println("Output:");
        System.out.println("| Hashtag     | Count |");
        System.out.println("|-------------|-------|");
        for (HashtagCount hc : hashtags) {
            System.out.printf("| %-11s | %5d |\n", hc.hashtag, hc.count);
        }
    }

    public static void main(String[] args) {
        List<String> tweets = Arrays.asList(
            "Tweet 13 #HappyDay",
            "Tweet 14 #HappyDay!",
            "Tweet 17 #HAPPYDAY",
            "Tweet 15 #TechLife",
            "Tweet 18 #TechLife!"
        );

        List<HashtagCount> result = countHashtags(tweets);
        printHashtagTable(result);
    }
}
