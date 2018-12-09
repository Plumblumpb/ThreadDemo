package A8ThreadLocal;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Auther: cpb
 * @Date: 2018/9/30 14:57
 * @Description:
 */
public class ThreadLocalTest {
    public static void main(String[] args) {
    }

    public void threadlocal(){
        final int threadLocalCount = 1000;
        final ThreadLocal<String>[] caches = new ThreadLocal[threadLocalCount];
        final Thread mainThread = Thread.currentThread();
        for (int i=0;i<threadLocalCount;i++) {
            caches[i] = new ThreadLocal();
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<threadLocalCount;i++) {
                    caches[i].set("float.lu");
                }
                long start = System.nanoTime();
                for (int i=0;i<threadLocalCount;i++) {
                    for (int j=0;j<1000000;j++) {
                        caches[i].get();
                    }
                }
                long end = System.nanoTime();
                System.out.println("take[" + TimeUnit.NANOSECONDS.toMillis(end - start) +
                        "]ms");
                LockSupport.unpark(mainThread);
            }

        });
        t.start();
        LockSupport.park(mainThread);
    }

//    public void fastThreadlocal(){
//        final int threadLocalCount = 1000;
//        final FastThreadLocal<String>[] caches = new FastThreadLocal[threadLocalCount];
//        final Thread mainThread = Thread.currentThread();
//        for (int i=0;i<threadLocalCount;i++) {
//            caches[i] = new FastThreadLocal();
//        }
//        Thread t = new FastThreadLocalThread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i=0;i<threadLocalCount;i++) {
//                    caches[i].set("float.lu");
//                }
//                long start = System.nanoTime();
//                for (int i=0;i<threadLocalCount;i++) {
//                    for (int j=0;j<1000000;j++) {
//                        caches[i].get();
//                    }
//                }
//                long end = System.nanoTime();
//                System.out.println("take[" + TimeUnit.NANOSECONDS.toMillis(end - start) +
//                        "]ms");
//                LockSupport.unpark(mainThread);
//            }
//
//        });
//        t.start();
//        LockSupport.park(mainThread);
//    }
}
