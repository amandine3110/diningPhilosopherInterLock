package diningphilosophers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {

    private static int stickCount = 0;

    private boolean iAmFree = true;
    private final int myNumber;
    private final Lock lock = new ReentrantLock();
    private final Condition disponible = lock.newCondition();
    private final Condition pasDisponible = lock.newCondition();


    public ChopStick() {
        myNumber = ++stickCount;
    }

    public boolean tryTake() throws InterruptedException {
        lock.lock();
        try {
            while (!iAmFree) {
                disponible.await();
            }
            iAmFree = false;
            System.out.println("Stick " + myNumber + " Taken");
            return true;
        }finally {
            lock.unlock();
        }
    }

    public void release() {
        lock.lock();
        try {
            System.out.println("Stick " + myNumber + " Released");
            iAmFree = true;
            disponible.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
