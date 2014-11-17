package ActiveMonitor;

import java.util.Comparator;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class PredicateTag {
    public enum OperationType {
        EQ, NEQ, GT, GTE, LT, LTE, C, EC, NONE
    }

    public static class GTEComparator implements Comparator<PredicateTag> {
        @Override public int compare(PredicateTag a, PredicateTag b) {
            if (a.val > b.val) {
                return 1;
            } else if (a.val < b.val) {
                return -1;
            } else {
                if (a.type == OperationType.GT) {
                    return -1; 
                } else {
                    return 1;
                }
            }
        }
    }

    public static class LTEComparator implements Comparator<PredicateTag> {
        @Override public int compare(PredicateTag a, PredicateTag b) {
            if (a.getVal() > b.getVal()) {
                return -1;
            } else if (a.getVal() < b.getVal()) {
                return 1;
            } else {
                if (a.type == OperationType.GT) {
                    return 1; 
                } else {
                    return -1;
                }
            }
        }
    }
    
    //private final String key;
    public final String varName;
    public final int val;
    public final OperationType type;
    
    private final SafeTaskManager mger;
    private final Assertion assertion;

    private final ConcurrentMap<ConditionTaskQueue, ConditionTaskQueue> mapQueue;
        


    public PredicateTag(String varName, int val, OperationType type, 
            Assertion assertion, SafeTaskManager mger) {
        this.varName = varName;
        this.type = type;
        this.val = val;
        this.mger = mger;
        this.assertion = assertion;

        mapQueue = new ConcurrentHashMap<ConditionTaskQueue, ConditionTaskQueue>();
    }

    public int getVal() {
        return val;
    }
   
    public boolean isTrue() {
        return assertion.isTrue();
    }

    public void addQueue(ConditionTaskQueue queue) {
        if (mapQueue.size() == 0) {
            mger.addTag(this);
        }
        mapQueue.putIfAbsent(queue, queue);
        //System.out.println("add queue in tag");
    }

    public void removeQueue(ConditionTaskQueue queue, String key) {
        mapQueue.remove(queue);
        if (mapQueue.size() == 0) {
            mger.removeTag(this, key); 
        }
    }

    public ActiveTask nextTask() {
        for (ConditionTaskQueue queue: mapQueue.keySet()) {
            if (queue.assertionIsTrue() && queue.numOfTask() > 0) {
                return queue.getTask();
            } 
        }
        return null;
    }
}
