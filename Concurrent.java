class CounterT extends Thread {
    private static int c = 0;
    private static final Object l = new Object();

    @Override
    //this increases the counter 100 times
    public void run() {
        for (int i = 0; i < 100; i++) {
            synchronized (l) {
                c++;
            }
        }
    }

    public static int getCounter() {
        return c;// this will give the counter
    }
}

public class Concurrent {
    public static void main(String[] args) {
        CounterT t1 = new CounterT();
        CounterT t2 = new CounterT();
        CounterT t3 = new CounterT();
        //calling these 3 times will result in 300
        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("Final counter: " + CounterT.getCounter());
    }
}
