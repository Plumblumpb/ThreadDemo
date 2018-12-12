package A4_1AQS源码分析;

import sun.security.util.Cache;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: cpb
 * @Date: 2018/12/10 18:39
 * @Description:
 */
public class Main implements  Runnable{

    private ReentrantLock lock = new ReentrantLock();

    private int c;
    public void  test(){
        System.out.println(c);
    }



    public static void main(String[] args) {
        Main  main = new Main() ;
        for(int i = 0 ; i<100; i++){
            Thread thread = new Thread(main);
           thread.start();
       }
    }

    @Override
    public void run() {
        try {
            lock.lock();
            c++;
            System.out.println(c);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


//    AbstractQueuedSynchronizer
//            ReentrantLock

}
