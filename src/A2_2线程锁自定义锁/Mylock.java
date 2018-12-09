package A2_2线程锁自定义锁;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created with IDEA
 * author:plumblum
 * Date:2018/12/8
 * Time:23:51
 */
public class Mylock  implements Lock{

//    是否带锁
    private boolean isLocked = false;
//    当前线程
    private Thread lock = null;
//     重入锁次数
    private int lockCount = 0;


    @Override
    public synchronized void lock() {
        Thread lockThread = Thread.currentThread();
//        是否为当前锁或者重入锁
        while(isLocked && lockThread == lock){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        设置锁
        isLocked = true;
//        重入次数+1
        lockCount++;

        lock = lockThread;
    }

    @Override
    public synchronized void unlock() {

        while(lock == Thread.currentThread()){
//            重入次数-1；
            lockCount--;
            if(lockCount == 0){
                notify();
                isLocked = false;
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }
}
