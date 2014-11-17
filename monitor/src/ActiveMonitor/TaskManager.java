package ActiveMonitor;

import java.util.concurrent.locks.LockSupport;


public abstract class TaskManager {
    abstract public ActiveTask nextTask();
    private Thread runner;

    public void setRunner(Thread runner) {
        this.runner = runner;
    }

    public void signalManager() {
        LockSupport.unpark(runner);
    }
}
