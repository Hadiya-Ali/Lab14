import java.util.concurrent.CopyOnWriteArrayList;

class WriterT extends Thread {
    private final CopyOnWriteArrayList<Integer> l;

    public WriterT(CopyOnWriteArrayList<Integer> l) {
        this.l = l;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10; j++) {
            l.add(j); 
            System.out.println(Thread.currentThread().getName() + " adding: " + j);
            try {
                Thread.sleep(100); // will add delay in the thread
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

class ReaderT extends Thread {
    private final CopyOnWriteArrayList<Integer> l;

    public ReaderT(CopyOnWriteArrayList<Integer> l) {
        this.l = l;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10; j++) {
            System.out.println(Thread.currentThread().getName() + " reading: " + l);
            try {
                Thread.sleep(50);//delayyy
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

public class ConcurrentData {
    public static void main(String[] args) {
        
        CopyOnWriteArrayList<Integer> sharedL = new CopyOnWriteArrayList<>();

        
        Thread w1 = new WriterT(sharedL);
        Thread w2 = new WriterT(sharedL);

       
        Thread r1 = new ReaderT(sharedL);
        Thread r2 = new ReaderT(sharedL);

       
        w1.start();
        w2.start();
        r1.start();
        r2.start();

        
        try {
            w1.join();
            w2.join();
            r1.join();
            r2.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("Final list: " + sharedL);
    }
}
