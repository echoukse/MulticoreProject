
package examples.Stack;

import ActiveMonitor.ActiveTask;

public abstract class Stack<T extends Comparable<T>> {
    public abstract ActiveTask<Object> insert(T val); 

    public abstract ActiveTask<Object> remove(T val); 
    
}
