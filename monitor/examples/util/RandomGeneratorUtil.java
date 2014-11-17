package examples.util;

import java.util.Random;

public class RandomGeneratorUtil {
    public static final byte INSERT = 0;
    public static final byte DELETE = 1;
    public static final byte SEARCH = 2;
    public static byte[] generateOps(int randomSeed, int totalOps,
                float insertPerc, float deletePerc) {
        byte[] ops = new byte[totalOps];
        Random rand = new Random(randomSeed);
        for(int i=0;i<totalOps;i++){
            float rf = rand.nextFloat();
            if(rf < insertPerc){
                ops[i] = INSERT;
                continue;
            }
            if(rf < insertPerc + deletePerc){
                ops[i] = DELETE;
                continue;
            }
            ops[i] = SEARCH;
        }
        return ops;
    }

    public static int[] generateVals(int randomSeed, int totalVal,
                int keyCeiling) {
        int[] vals = new int[totalVal];

        Random rand = new Random(randomSeed);
        for (int i = 0; i < totalVal; i++) {
            vals[i] = rand.nextInt(keyCeiling);
        }

        return vals;
    }

    public static int[] getOpCounts(byte[] ops){
        int zerocount = 0, onecount = 0, twocount = 0;
        for(byte b : ops){
            switch (b) {
                case INSERT:
                    zerocount++;
                    break;
                case DELETE: 
                    onecount++;
                    break;
                case SEARCH:
                    twocount++;
                    break;
                default:
                    break;
            }
        }
        int[] counts = new int[]{zerocount, onecount, twocount};
        //System.out.println(zerocount+" inserts "+ onecount+" deletes "+ twocount+ " seeks.");
        return counts;
    }

    public static void main(String[] args) {
        byte[] ops = generateOps(8971351, 100, 0.05f, 0.11f);
        getOpCounts(ops);
    }
}
