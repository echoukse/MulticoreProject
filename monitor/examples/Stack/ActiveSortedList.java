package examples.Stack;

import java.util.LinkedList;

import ActiveMonitor.*;
import util.Common;

public class ActiveSortedList<T extends Comparable<T>> extends SortedList<T> {
    LinkedList<T> list = new LinkedList<T>();
    private final MonitorThreadPool executor;
    private final SafeTaskManager manager;

    public ActiveSortedList() {
        executor = MonitorThreadPool.getInstance();
        manager = new SafeTaskManager(128);
        executor.registerTaskManager(this, manager);
    }
    
    public ActiveTask<Object> insert(final T val) {
        ActiveTask<Object> worker = new ActiveTask<Object>() {
            public Object call() {
                list.push(val);
                //System.out.println("Insert val: " + val + " to this list");
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
                //System.out.println("remove val: " + val);
                return null;
            }
        };
        
        manager.addNoConditionTask(worker);

        return worker;
    }

}
