/*
 * Classname
 *
 * Version info
 *
 * Copyright notice
 */
package util;

public class Common {
    public static void println(String str) {
        if (Symbols.isDebug) {
            System.out.println(str);
        }
    }

    public static void isBug(boolean cond) {
        if(cond) {
            System.out.println("Quack!  in thread " + Thread.currentThread().getName() +
                    " file " + new Throwable().getStackTrace()[1].getFileName() +
                    " class " + new Throwable().getStackTrace()[1].getClassName() +
                    " method " + new Throwable().getStackTrace()[1].getMethodName() +
                    " line " + new Throwable().getStackTrace()[1].getLineNumber());

            while(true);
        }
    }
}
