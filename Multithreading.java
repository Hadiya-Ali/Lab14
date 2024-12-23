// Thread that prints numbers from 1 to 10
class Number extends Thread {
    private final Object l;

    public Number(Object l) {
        this.l = l;
    }

    public void run() {
        // Print numbers from 1 to 10
        for (int i = 1; i <= 10; i++) {
            synchronized (l) {
                System.out.println("Number: " + i); // This will print the number
                l.notify(); // Notify the other thread
                try {
                    if (i < 10) l.wait();  // Wait for the square thread to print
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

// Runnable class that prints squares of numbers from 1 to 10
class Square implements Runnable {
    private final Object l;

    public Square(Object l) {
        this.l = l;
    }

    public void run() {
        // Print squares of numbers from 1 to 10
        for (int i = 1; i <= 10; i++) {
            synchronized (l) {
                System.out.println("Square "+ ": " + (i * i)); // This will print the square
                l.notify();  // Notify the other thread
                try {
                    if (i < 10) l.wait();  // Wait for the number thread to print
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }
    }
}

public class Multithreading {
    public static void main(String[] args) {
        Object lock = new Object();  // Shared lock object for synchronization

        // Create and start the Number thread
        Number number = new Number(lock);
        number.start();

        // Create and start the Square thread
        Square square = new Square(lock);
        Thread squareThread = new Thread(square);
        squareThread.start();

        // Ensure both threads finish before the main thread exits
        try {
            number.join();
            squareThread.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("The end");
    }
}
