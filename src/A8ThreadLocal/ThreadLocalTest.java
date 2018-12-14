package A8ThreadLocal;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @Auther: cpb
 * @Date: 2018/9/30 14:57
 * @Description:
 */
public class ThreadLocalTest {
    private int value;

    private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public void set(int value){
        threadLocal.set(value);
    }

    public  Integer get(){
        Integer value = threadLocal.get();
        value++;
        threadLocal.set(value);
        return  value;
    }

    public static void main(String[] args) {
            ThreadLocalTest test = new ThreadLocalTest();

        new Thread(new Runnable() {
            @Override
            public void run() {
                test.set(0);
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " " + test.get());
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                test.set(10);
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " " + test.get());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


}
