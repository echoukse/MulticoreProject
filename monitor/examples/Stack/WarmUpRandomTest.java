
package examples.Stack;

import java.util.concurrent.CyclicBarrier;

import examples.util.Global;
import examples.util.LocalOperationUtil;
import examples.util.RandomGeneratorUtil;

import ActiveMonitor.ActiveTask;


public class WarmUpRandomTest {
    static final int [] primes = {
        217645199, 236887691,
        236887699, 256203161,
        256203221, 275604541,
        275604547, 295075147,
        295075153, 314606869,
        314606891, 334214459,
        334214467, 353868013,
        353868019, 373587883,
        373587911, 393342739,
        393342743, 413158511,
        413158523, 433024223,
        433024253, 452930459,
        452930477, 472882030
    };

    static final int WARM_UP = 1000;
    static class Data {
        long startTime;
        long endTime;

        public long getExecuteTime() {
            return endTime - startTime;
        }
    }

    static class TestThread extends Thread {
        private Stack<Integer> list;
        private int numSinOp;
        private byte[] warmUpOps;
        private int[] warmUpVals;
        private byte[] expOps;
        private int[] expVals;
        private CyclicBarrier warmUpBarrier;
        private CyclicBarrier expBarrier;

        public TestThread(Stack<Integer> list, int numSinOp,
                byte[] warmUpOps, int[] warmUpVals, byte[] expOps,
                int[] expVals, CyclicBarrier warmUpBarrier,
                CyclicBarrier expBarrier) {
            this.list = list;
            this.numSinOp = numSinOp;
            this.warmUpOps = warmUpOps;
            this.warmUpVals = warmUpVals;
            this.expOps = expOps;
            this.expVals = expVals;
            this.warmUpBarrier = warmUpBarrier;
            this.expBarrier = expBarrier;
        }

        public void run() {                                                     

            ActiveTask<Object> ret = null;
            for (int i = 0; i < WARM_UP; i++) {

                if (warmUpOps[i] == RandomGeneratorUtil.INSERT) {
                    ret = list.insert(warmUpVals[i]);
                } else {
                    ret = list.remove(warmUpVals[i]);
                }
            }
            if (ret != null) {
                try {
                    ret.get(); 
                } catch(Exception e) {
                }
            }
            
            try {
                warmUpBarrier.await();
            } catch(Exception e) {
            }

            for (int i = 0; i < expOps.length; i++) {

                if (expOps[i] == RandomGeneratorUtil.INSERT) {
                    ret = list.insert(expVals[i]);
                } else {
                    ret = list.remove(expVals[i]);
                }
            }
            if (ret != null) {
                try {
                    ret.get(); 
                } catch(Exception e) {
                }
            }

            try {
                expBarrier.await();
            } catch(Exception e) {
            }
        }                                                     

    }

    public static void main (String[] args) {
        int listInitSize = 0; // Stack init size
        int numSinOp = 0; // # of ops outside CS
        int numIteration = Integer.parseInt(args[0]); // Total # ops
        int numThread = Integer.parseInt(args[1]);
        float insertPercent = Float.parseFloat(args[2]) / 100.0f;
        float removePercent = Float.parseFloat(args[3]) / 100.0f;
        int keyCeiling = Integer.MAX_VALUE; // Highest value to use for inserting

        Stack<Integer> list = null;
        if(args.length > 4) {
            list = new ActiveStack<Integer>(Integer.parseInt(args[4]));
        } else {
            list = new ActiveStack<Integer>();
        }

        // list init 
        int[] vals = RandomGeneratorUtil.generateVals(primes[24], listInitSize,
                keyCeiling);
        for (int i = 0; i < listInitSize; i++) {
            list.insert(vals[i]);
        }

        Thread[] threads = new Thread[numThread];

        final Data data = new Data();
        CyclicBarrier warmUpBarrier = new CyclicBarrier(numThread,
                new Runnable() {
                    public void run() {
                        data.startTime = System.currentTimeMillis();
                    }
                });

        CyclicBarrier expBarrier = new CyclicBarrier(numThread + 1, 
                new Runnable() {
                    public void run() {
                        data.endTime = System.currentTimeMillis();
                    }
                });


        for (int i = 0; i < numThread; i++) {
            byte[] warmUpOps = RandomGeneratorUtil.generateOps(primes[i],
                    WARM_UP, insertPercent, removePercent);
            byte[] expOps = RandomGeneratorUtil.generateOps(primes[i],
                    numIteration, insertPercent, removePercent);
            int[] warmUpVals = RandomGeneratorUtil.generateVals(primes[i],
                    WARM_UP, keyCeiling);
            int[] expVals = RandomGeneratorUtil.generateVals(primes[i], 
                    numIteration, keyCeiling);
            threads[i] = new TestThread(list, numSinOp, warmUpOps, warmUpVals,
                    expOps, expVals, warmUpBarrier, expBarrier);
            threads[i].start();
        }                           

        try {
            expBarrier.await();
        } catch(Exception e) {
        }
        //list.printList();
        System.out.println(data.getExecuteTime());
        System.exit(0);
    }
}
