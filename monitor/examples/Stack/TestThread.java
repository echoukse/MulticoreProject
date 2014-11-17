
package examples.Stack;
import java.util.concurrent.ExecutionException;

import ActiveMonitor.ActiveTask;


public class TestThread extends Thread {
    SortedList<Integer> list;
    int size;
    int numListOp;
    int numSinOp;
    int numIteration;
    public TestThread(SortedList<Integer> list, int size, int numListOp, 
            int numSinOp, int numIteration) {
        this.list = list;                                                   
        this.numListOp = numListOp;                               
        this.numSinOp = numSinOp;                             
        this.numIteration = numIteration;
        this.size = size;
    }                                                                       

    @Override                                                               
    public void run() {                                                     

        ActiveTask<Object> ret = null;
        for (int i = 0; i < numIteration; i++) {                            
            
            for (int j = 0; j < numListOp; j++) {
                for (int k = 0; k < numSinOp; k++) {                            
                    Math.sin(Math.PI * 1.23);                                   
                }                                                               
                int val = (int) (Math.random() * size);
                list.remove(val);
                ret = list.insert(val);
            }
        }

        if (ret != null) {
            try {
                ret.get(); 
            } catch(Exception e) {
            }
        }
    }                                                                       
}  
