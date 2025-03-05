import java.util.*;
import java.util.concurrent.*;
import java.io.IOException;

// Represents a web page
class Page {
    String url;
    String content;
    boolean crawled;

    public Page(String url) {
        this.url = url;
        this.content = "";
        this.crawled = false;
    }
}

// Crawler task using Runnable
class Spider implements Runnable {
    private final String url;
    private final SmartCrawler crawler;

    public Spider(String url, SmartCrawler crawler) {
        this.url = url;
        this.crawler = crawler;
    }

    @Override
    public void run() {
        Page page = new Page(url);
        try {
            page.content = downloadPage(url);
            page.crawled = true;
            analyzePage(page);
            crawler.queueUrls(extractLinks(page.content));
            crawler.storePage(page); // Store results
        } catch (Exception e) {
            System.err.println("[Error] Failed to crawl: " + url);
        }
    }

    private String downloadPage(String url) throws IOException, InterruptedException {
        Thread.sleep(200);
        return "Data from " + url;
    }

    private void analyzePage(Page page) {
        System.out.println("[Analyzed] " + page.url);
    }

    private List<String> extractLinks(String content) {
        List<String> urls = new ArrayList<>();
        if (content.contains("example")) {
            urls.add("http://example.com/new" + new Random().nextInt(5));
        }
        return urls;
    }
}

// Manages the crawling
class SmartCrawler {
    private final BlockingQueue<String> queue;
    private final Set<String> visited;
    private final ExecutorService pool;
    private final Map<String, Page> results;

    public SmartCrawler(int threads, int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
        this.visited = ConcurrentHashMap.newKeySet();
        this.pool = Executors.newFixedThreadPool(threads);
        this.results = new ConcurrentHashMap<>();
    }

    public void queueUrls(List<String> urls) {
        for (String url : urls) {
            if (visited.add(url)) {
                queue.offer(url);
            }
        }
    }

    public void beginCrawl(List<String> startUrls) {
        queueUrls(startUrls);
        while (!queue.isEmpty()) {
            try {
                String url = queue.poll(2, TimeUnit.SECONDS);
                if (url != null) {
                    pool.execute(new Spider(url, this));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        pool.shutdown();
        try {
            pool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Error shutting down pool: " + e.getMessage());
        }
    }

    public void storePage(Page page) {
        results.put(page.url, page);
    }

    public void printResults() {
        System.out.println("\n[Crawled Results]");
        for (Map.Entry<String, Page> entry : results.entrySet()) {
            System.out.println("URL: " + entry.getKey() + " - Content: " + entry.getValue().content);
        }
    }
}

public class Multithreadwebcrawler {
    public static void main(String[] args) {
        SmartCrawler crawler = new SmartCrawler(5, 100);
        List<String> seedUrls = Arrays.asList("http://example.com", "http://example.com/page1");
        System.out.println("[Start] Crawling...");
        crawler.beginCrawl(seedUrls);
        crawler.printResults();
    }
}
