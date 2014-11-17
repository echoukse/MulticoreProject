package ActiveMonitor;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.LockSupport;

import util.Symbols;

public abstract class ActiveTask<T> implements Callable<T> {

    volatile private boolean isDone;
    volatile Thread submitter;
    private T result;

    public ActiveTask() {
        submitter = null;
        isDone = false;

    }

    public T get() {
        submitter = Thread.currentThread();

        
        for (;;) {
            int spin = Symbols.MAX_SPIN;
            while (!isDone && spin > 0) {
                --spin;
            }
            if (isDone) {
                break;
            }

            LockSupport.parkNanos(this, Symbols.PARK_TIMEOUT_NANO);
        }
        submitter = null;
        return result;
    }

    protected void execute() {

        T ret = null;
        try {
            ret = call();
        } catch(Exception e) {
            e.printStackTrace();
        }
        result = ret;
        isDone = true;
        if (submitter != null) {
            LockSupport.unpark(submitter);
        }
    }
}
