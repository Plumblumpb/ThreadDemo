package A2_1线程锁;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: cpb
 * @Date: 2018/9/17 17:27
 * @Description:
 */
public class MyLock  implements Runnable{
    private int i = 0;
    private volatile static  int j = 0;
    private int k = 0;

    ReentrantLock reentrantLock = new ReentrantLock();

    public  void run() {

        test1();
    }


    public void test1(){

        try {
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" i "+i++);
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" j "+j++);
            reentrantLock.lock();
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" k "+k++);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }

    }

    public static void main(String[] args) {
        MyLock myLock = new MyLock();

        for (int i = 0 ;i < 30 ; i++) {
            Thread thread = new Thread(myLock);
            thread.start();
        }
    }
}
