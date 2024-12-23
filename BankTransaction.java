import java.util.concurrent.atomic.AtomicInteger;
import java.util.Random;

class ClientTh extends Thread {
    private BankAcc acc;
    private Random r;

    public ClientTh(BankAcc acc) {
        this.acc = acc;
        this.r = new Random();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            boolean depositOpe = r.nextBoolean();
            int a = r.nextInt(100) + 1;

            if (depositOpe) {
                acc.deposit(a);
                System.out.println(Thread.currentThread().getName() + " depositing: " + a);
            } else {
                if (acc.withdraw(a)) {
                    System.out.println(Thread.currentThread().getName() + " withdrawing: " + a);
                } else {
                    System.out.println(Thread.currentThread().getName() + " failed to withdraw: " + a + " (Insufficient funds)");
                }
            }

            try {
                Thread.sleep(r.nextInt(100));
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

class BankAcc {
    private AtomicInteger bal;

    public BankAcc(int initialBal) {
        bal = new AtomicInteger(initialBal);
    }

    public void deposit(int amount) {
        bal.addAndGet(amount);
    }

    public boolean withdraw(int a) {
        int currentBal = bal.get();
        if (currentBal >= a) {
            bal.addAndGet(-a);
            return true;
        }
        return false;
    }

    public int getBal() {
        return bal.get();
    }
}

public class BankTransaction {
    public static void main(String[] args) {
        BankAcc acc = new BankAcc(1000);

        ClientTh c1 = new ClientTh(acc);
        ClientTh c2 = new ClientTh(acc);
        ClientTh c3 = new ClientTh(acc);
        ClientTh c4 = new ClientTh(acc);

        c1.start();
        c2.start();
        c3.start();
        c4.start();

        try {
            c1.join();
            c2.join();
            c3.join();
            c4.join();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        System.out.println("Final Account Balance: " + acc.getBal());
    }
}
