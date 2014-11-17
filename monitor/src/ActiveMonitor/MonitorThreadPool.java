package ActiveMonitor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import util.Symbols;


// This class is Singleton 
public class MonitorThreadPool {
    public static MonitorThreadPool getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public long getNumCompleteTask() {
        return numCompleteTask.get();
    }
    public void registerTaskManager(Object monitor, TaskManager manager) {
        if (mapMonitorTaskManager.putIfAbsent(monitor, manager) == null) {
            Thread thread = new Thread(new Worker(manager));
            manager.setRunner(thread);
            thread.start();
        }
    }

    private final class Worker implements Runnable {

        public Worker(TaskManager manager) {
            this.manager = manager;
        }
        public void run() {
            for(;;) {
                ActiveTask command = manager.nextTask();
                int spin = Symbols.MAX_SPIN;
                while (command == null && spin > 0) {
                    command = manager.nextTask();
                    --spin;
                    Thread.yield();
                }
                if (command != null) {
                    try {
                        //command.run();
                        command.execute();
                        //System.out.println("execute task");
                        //numCompleteTask.getAndIncrement();
                    } catch(Exception e) {
                            e.printStackTrace();
                    }
                } else {
                    LockSupport.parkNanos(this, Symbols.PARK_TIMEOUT_NANO);
                }

            }
        }

        private final TaskManager manager;
    }

    private final ConcurrentHashMap<Object, TaskManager> mapMonitorTaskManager;

    private MonitorThreadPool() {
        mapMonitorTaskManager = new ConcurrentHashMap<Object, TaskManager>();
    }
    
    private static class SingletonHolder {
        private static final MonitorThreadPool INSTANCE 
                = new MonitorThreadPool();
    }

    private AtomicLong numCompleteTask = new AtomicLong(0);
}
