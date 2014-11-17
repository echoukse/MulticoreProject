// Assume that a TaskManager object will belong to only one monitor thread 


package ActiveMonitor;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.RunnableFuture;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

//import java.util.concurrent.LinkedBlockingQueue;
import util.LinkedBlockingQueue;

public class SafeTaskManager extends TaskManager {

    public SafeTaskManager() {
        this(0);
    }
    public SafeTaskManager(int queueSize) {
        this.queueSize = queueSize;
        if (queueSize <= 0) {
            tasksNoCond = new LinkedBlockingQueue<ActiveTask>();
        } else {
            tasksNoCond = new LinkedBlockingQueue<ActiveTask>(queueSize);
        }
        //tasksNoCond = new LinkedBlockingQueue<Runnable>();
        //tasksNoCond = new ConcurrentLinkedQueue<Runnable>();
    }
    public ActiveTask nextTask() {

        // Tautology
        ActiveTask ret = tasksNoCond.poll();

        if (ret != null) return ret;

        // equivalence
        for (Entry<String, ConcurrentMap<Integer, PredicateTag>> entry :              
                mapEPTag.entrySet()) {                                          

            PredicateTag tag = entry.getValue().get(                            
                    mapGlobalVar.get(entry.getKey()).getValue());               

            if (tag != null) {
                ret = tag.nextTask();

                if (ret != null) {
                    return ret;                                                     
                }                                                               
            }                                                                   
        } 

        // lteptag

/*
 *        for (Entry<String, PriorityBlockingQueue<PredicateTag>> entry :                 
 *                mapLTEPTag.entrySet()) {                                        
 *            PredicateTag tag = entry.getValue().peek();                         
 *                                                                                
 *
 *            LinkedList<PredicateTag> tags = new LinkedList<PredicateTag>();
 *            while (tag != null && tag.isTrue()) {                                             
 *                ret = tag.nextTask();
 *                if (ret != null) {                                 
 *                    // add false tags back                                      
 *                    for (PredicateTag trueTag : tags) {                         
 *                        entry.getValue().add(trueTag);                          
 *                    }                                                           
 *                    return ret;                                                     
 *                } else {  // tage is ture but no predicates is true             
 *                    tags.add(entry.getValue().poll());                          
 *                    tag = entry.getValue().peek();                              
 *                }                                                               
 *            } 
 *            // add false tags back                                      
 *            for (PredicateTag trueTag : tags) {                         
 *                entry.getValue().add(trueTag);                          
 *            }                                                           
 *        }
 *
 *        // gteptag
 *
 *        for (Entry<String, PriorityBlockingQueue<PredicateTag>> entry :                 
 *                mapGTEPTag.entrySet()) {                                        
 *            PredicateTag tag = entry.getValue().peek();                         
 *                                                                                
 *            LinkedList<PredicateTag> tags = new LinkedList<PredicateTag>();
 *            while (tag != null && tag.isTrue()) {                                             
 *                ret = tag.nextTask();
 *                if (ret != null) {                                 
 *                    // add false tags back                                      
 *                    for (PredicateTag trueTag : tags) {                         
 *                        entry.getValue().add(trueTag);                          
 *                    }                                                           
 *                    return ret;                                                     
 *                } else {  // tage is ture but no predicates is true             
 *                    tags.add(entry.getValue().poll());                          
 *                    tag = entry.getValue().peek();                              
 *                }                                                               
 *            } 
 *            // add false tags back                                      
 *            for (PredicateTag trueTag : tags) {                         
 *                entry.getValue().add(trueTag);                          
 *            }                                                           
 *        }
 */





        // complex
        for (ConditionTaskQueue queue: mapCompQueue.keySet()) {
            if (queue.assertionIsTrue() && queue.numOfTask() > 0) {
                return queue.getTask();
            } 
        }

        return null;
    }

    public void registerGlobalVariable(GlobalVariable var) {
        mapGlobalVar.putIfAbsent(var.name, var);
    }

    public Future<?> submit(Object monitor, Runnable task, Assertion assertion,
            boolean isGlobal, PredicateTag[] tags) {
        return null;
    }

    public <T> Future<T> submit(Object monitor, Callable task, 
            Assertion assertion, boolean isGlobal, PredicateTag[] tags) {
        return null;
    }

    private <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new FutureTask<T>(runnable, value);
    }

    private <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask<T>(callable);
    }

    public <T> void addNoConditionTask(ActiveTask<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        try {
            tasksNoCond.put(task);
        } catch(Exception e) {
        }
        signalManager();
    }

    public void removeConditionTaskQueue(String key) {
        mapTaskQueue.remove(key);
    }

    public ConditionTaskQueue makeConditionTaskQueue(String key, 
            Assertion assertion, boolean isGlobal, PredicateTag[] tags) {

        ConditionTaskQueue ret = mapTaskQueue.get(key);

        if (ret != null) {
            return ret;
        } 

        ConditionTaskQueue queue = new ConditionTaskQueue(assertion, tags, this, queueSize);
        ret = mapTaskQueue.putIfAbsent(key, queue);
        if (ret == null) {
            return queue;
        }

        return ret;
    }

    public void addComplexConditionTask(ConditionTaskQueue queue) {
        mapCompQueue.putIfAbsent(queue, queue);
        signalManager();
    }

    public void removeComplexConditionTask(ConditionTaskQueue queue) {
        if (queue.numOfTask() == 0) {
            mapCompQueue.remove(queue);
        }
    }

    // not yet implement 
    public void addTag(PredicateTag tag) {
        switch (tag.type) {
            case EQ:
                ConcurrentMap<Integer, PredicateTag> mapOriginalTag 
                    = mapEPTag.get(tag.varName);
                if (mapOriginalTag == null) {
                    mapOriginalTag = new ConcurrentHashMap<Integer, PredicateTag>();
                    ConcurrentMap<Integer, PredicateTag> mapOldTag = 
                        mapEPTag.putIfAbsent(tag.varName, mapOriginalTag);

                    if (mapOldTag != null) {
                        mapOriginalTag = mapOldTag;
                    }
                }
                mapOriginalTag.putIfAbsent(tag.val, tag);
                break;
            case NEQ:
                break;
            case GT:
            case GTE:
                PriorityBlockingQueue<PredicateTag> queueOriginalTag
                    = mapGTEPTag.get(tag.varName);

                if (queueOriginalTag == null) {
                    queueOriginalTag = new PriorityBlockingQueue<PredicateTag>(10, gteCmp);
                    PriorityBlockingQueue<PredicateTag> queueOldTag = 
                        mapGTEPTag.putIfAbsent(tag.varName, queueOriginalTag);

                    if (queueOldTag != null) {
                        queueOriginalTag = queueOldTag;
                    }
                }

                queueOriginalTag.put(tag);

                break;
            case LT:
            case LTE:
                queueOriginalTag
                    = mapLTEPTag.get(tag.varName);

                if (queueOriginalTag == null) {
                    queueOriginalTag = new PriorityBlockingQueue<PredicateTag>(10, lteCmp);
                    PriorityBlockingQueue<PredicateTag> queueOldTag = 
                        mapLTEPTag.putIfAbsent(tag.varName, queueOriginalTag);

                    if (queueOldTag != null) {
                        queueOriginalTag = queueOldTag;
                    }
                }

                queueOriginalTag.put(tag);

                break;
            default:
                break;
        }
    }

    public PredicateTag makeTag(String key, String varName, int val, 
            PredicateTag.OperationType type, Assertion assertion) {

        PredicateTag ret = mapTag.get(key);

        if (ret == null) {
            ret = new PredicateTag(varName, val, type, assertion, this);
            PredicateTag tag = mapTag.putIfAbsent(key, ret);
            if (tag != null) {
                ret = tag;
            }
        }
        return ret;
    }
    public void removeTag(PredicateTag tag, String key) {
        switch (tag.type) {                                                     
            case EQ:                                                            
                mapEPTag.get(tag.varName).remove(tag.val);                      
                break;                                                          
            case NEQ:                                                           
                break;                                                          
            case GT:                                                            
            case GTE:                                                           
                mapGTEPTag.get(tag.varName).remove(tag);                        
                break;                                                          
            case LT:                                                            
            case LTE:                                                           
                mapLTEPTag.get(tag.varName).remove(tag);                        
                break;                                                          
            default:                                                            
                break;                                                          
        } 
        mapTag.remove(key);
    }
    public void removeTag(String key) {
        mapTag.remove(key);
    }

    
    // complex predicate
    private final ConcurrentMap<ConditionTaskQueue, ConditionTaskQueue> mapCompQueue 
        = new ConcurrentHashMap<ConditionTaskQueue, ConditionTaskQueue>();
    //private final LinkedBlockingQueue<ConditionTaskQueue> listCompQueue 
        //= new LinkedBlockingQueue<ConditionTaskQueue>();



    // tag  collection
    private final ConcurrentMap<String, PredicateTag> mapTag
        = new ConcurrentHashMap<String, PredicateTag>(); 

    private final ConcurrentMap<String, GlobalVariable> mapGlobalVar 
        = new ConcurrentHashMap<String, GlobalVariable>();


    // equivalence predicate 
    private final ConcurrentMap<String, ConcurrentMap<Integer, PredicateTag>> mapEPTag 
        = new ConcurrentHashMap<String, ConcurrentMap<Integer, PredicateTag>>();


    // not yet 
    private final ConcurrentMap<String, PriorityBlockingQueue<PredicateTag>> mapGTEPTag
        = new  ConcurrentHashMap<String, PriorityBlockingQueue<PredicateTag>>();
    private final ConcurrentMap<String, PriorityBlockingQueue<PredicateTag>> mapLTEPTag
        = new ConcurrentHashMap<String, PriorityBlockingQueue<PredicateTag>>();

    // ordering predicate
    private final Comparator<PredicateTag> gteCmp 
        = new PredicateTag.GTEComparator();
    private final Comparator<PredicateTag> lteCmp 
        = new PredicateTag.LTEComparator();

    // predicate collection
    private final ConcurrentMap<String, ConditionTaskQueue> mapTaskQueue 
        = new ConcurrentHashMap<String, ConditionTaskQueue>();

    private final LinkedBlockingQueue<ActiveTask> tasksNoCond;
    //private final Queue<Runnable> tasksNoCond;
        //= new ConcurrentLinkedQueue<Runnable>();

    private final int queueSize;
}
