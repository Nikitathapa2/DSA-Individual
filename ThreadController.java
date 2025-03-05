import java.util.Scanner;
import java.util.concurrent.Semaphore;

class NumberPrinter {
    public void printZero() {
        System.out.print("0");
    }

    public void printEven(int x) {
        System.out.print(x);
    }

    public void printOdd(int x) {
        System.out.print(x);
    }
}

class ThreadController {
    private int n;
    private NumberPrinter printer;
    private Semaphore zeroSemaphore = new Semaphore(1);
    private Semaphore oddSemaphore = new Semaphore(0);
    private Semaphore evenSemaphore = new Semaphore(0);

    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    public void printZero() throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            zeroSemaphore.acquire();
            printer.printZero();
            if (i % 2 == 0) {
                evenSemaphore.release();
            } else {
                oddSemaphore.release();
            }
        }
    }

    public void printEven() throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            evenSemaphore.acquire();
            printer.printEven(i);
            zeroSemaphore.release();
        }
    }

    public void printOdd() throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            oddSemaphore.acquire();
            printer.printOdd(i);
            zeroSemaphore.release();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter n: ");
        int n = scanner.nextInt();
        scanner.close();

        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);

        Thread zeroThread = new Thread(() -> {
            try {
                controller.printZero();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread evenThread = new Thread(() -> {
            try {
                controller.printEven();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread oddThread = new Thread(() -> {
            try {
                controller.printOdd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}
