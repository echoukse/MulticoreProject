package examples.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/** Count the number of threads that complete a task.
 * One thread can wait until they are all done.
 */

public class DoneCounter {
    int count = 0 ;
    int goal = 0 ;
    final Lock mutex = new ReentrantLock();
    final Condition done = mutex.newCondition(); 

    public void set( int val ) {
        mutex.lock();
        goal = val ;
        count = 0 ;
        mutex.unlock(); 
    }

    public int increment() {
        mutex.lock();
        count += 1 ;
        int value = count ;
        done.signal();
        mutex.unlock(); 
        return value ;
    }

    public void waitForDone() {
        mutex.lock();
        while(goal != count) {
            try {
                done.await();
            }
            catch(InterruptedException e) {

            }
        }
        mutex.unlock(); 
    }
}
