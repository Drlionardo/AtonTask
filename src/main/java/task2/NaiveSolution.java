package task2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NaiveSolution {
    private Lock lock;
    private Condition canSing;
    private final String[][] lyrics;

    public NaiveSolution(String[][] lyrics) {
        this.lyrics = lyrics;
        lock = new ReentrantLock();
        canSing = lock.newCondition();
    }

    public void sing() {
        String name = Thread.currentThread().getName();
        try {
            lock.lock();
            for (int i = 0; i < lyrics.length; i++) {
                if (lyrics[i][0].contains(name)) {
                    System.out.printf("%s: %s\n", name, lyrics[i][1]);
                }
                canSing.signalAll();
                if(!isLastLine(i)) {
                    canSing.await();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private boolean isLastLine(int i) {
        return i == lyrics.length - 1;
    }
}
