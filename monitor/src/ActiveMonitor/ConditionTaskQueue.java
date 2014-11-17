package ActiveMonitor;

import java.util.concurrent.BlockingQueue;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RunnableFuture;

//import java.util.concurrent.LinkedBlockingQueue;
import util.LinkedBlockingQueue;

public class ConditionTaskQueue {

    public ConditionTaskQueue(Assertion assertion, PredicateTag[] tags, 
            TaskManager manager) {
        this(assertion, tags, manager, 0);
    }
    public ConditionTaskQueue(Assertion assertion, PredicateTag[] tags, 
            TaskManager manager, int queueSize) {
        this.assertion = assertion;
        this.tags = tags;
        this.manager = manager;

        if (queueSize <= 0) {
            tasks = new LinkedBlockingQueue<ActiveTask>();
        } else {
            tasks = new LinkedBlockingQueue<ActiveTask>(queueSize);
        }
        //tasks = new LinkedBlockingQueue<Runnable>();
        //tasks = new ConcurrentLinkedQueue<Runnable>();
    }

    // submit
    public <T> void addTask(ActiveTask<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        //RunnableFuture<T> ftask = newTaskFor(task);
        //tasks.add(ftask);
        try {
            tasks.put(task);
        } catch(Exception e) {
        }
        manager.signalManager();
    }
    
    public boolean assertionIsTrue() {
        return assertion.isTrue();
    }

    public int numOfTask() {
        return tasks.size();
    }

    public ActiveTask getTask() {
        return tasks.poll();
    }

    private <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new FutureTask<T>(runnable, value);
    }

    private <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask<T>(callable);
    }

    private final Assertion assertion;
    private final PredicateTag[] tags;
    private final LinkedBlockingQueue<ActiveTask> tasks;
    //private final Queue<Runnable> tasks;
    private final TaskManager manager;

}
