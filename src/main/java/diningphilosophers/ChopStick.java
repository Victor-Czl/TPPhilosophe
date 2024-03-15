package diningphilosophers;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ChopStick {

    private static int stickCount = 0;

    private boolean iAmFree = true;
    private final int myNumber;

    private final Lock verrou = new ReentrantLock();
    private final Condition conditionVerrou = verrou.newCondition();

    public ChopStick() {
        myNumber = ++stickCount;
    }

    public boolean take(int delais) throws InterruptedException {
        verrou.lock();
        try {
            while(!iAmFree) {
                conditionVerrou.await();
                return false;
            }
            iAmFree = false;
            return true;
        }
        finally {verrou.unlock();}
    }

    public void release() {
        verrou.lock();
        try {
            iAmFree = true;
            conditionVerrou.signalAll();
            System.out.println(toString() + "release");
        }
        finally {verrou.unlock();}
    }

    @Override
    public String toString() {
        return "Stick#" + myNumber;
    }
}
