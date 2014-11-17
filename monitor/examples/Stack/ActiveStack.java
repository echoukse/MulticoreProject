package examples.Stack;

import java.util.LinkedList;

import ActiveMonitor.*;
import util.Common;

public class ActiveStack<T extends Comparable<T>> extends Stack<T> {
    LinkedList<T> list = new LinkedList<T>();
    private final MonitorThreadPool executor;
    private final SafeTaskManager manager;

    public ActiveStack() {
        executor = MonitorThreadPool.getInstance();
        manager = new SafeTaskManager(256);
        executor.registerTaskManager(this, manager);
    }

    public ActiveStack(int stackSize) {
        executor = MonitorThreadPool.getInstance();
        manager = new SafeTaskManager(stackSize);
        executor.registerTaskManager(this, manager);
    }
    
    public ActiveTask<Object> insert(final T val) {
        ActiveTask<Object> worker = new ActiveTask<Object>() {
            public Object call() {
                list.push(val);
                return null;
            }
        };
        
        manager.addNoConditionTask(worker);

        return worker;
    }

    public ActiveTask<Object> remove(final T val) {
        ActiveTask<Object> worker = new ActiveTask<Object>() {
            public Object call() {
                if(list.size() > 0) {
                    list.pop();
                }
                return null;
            }
        };
        
        manager.addNoConditionTask(worker);

        return worker;
    }

}
