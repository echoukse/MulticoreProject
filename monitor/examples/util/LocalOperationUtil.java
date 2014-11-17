package examples.util;

import java.util.Random;

public class LocalOperationUtil {
    public static void performLocalSinOperation(int num) {
        //final int randomSeed = 2147483647;
        //Random rand = new Random(randomSeed);
        for (int i = 0; i < num; i++) {
            Math.sin(Math.PI * i);
        }
    }
}
