package A2_3AQS重入锁;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created with IDEA
 * author:plumblum
 * Date:2018/12/9
 * Time:21:12
 */
public class AQSLock {

    private AQS aqs = new AQS();

//    state  {0为无锁，1为持有锁}
    private   class AQS extends AbstractQueuedSynchronizer{


        @Override
//        获取锁
        protected boolean tryAcquire(int arg) {
//            1.如果第一个线程进入则获取锁
//            2.如果第二个线程进入则不能获取锁
//             3.如果判断是第一个线程还是其他线程呢（）
            int state = getState();
            Thread t = Thread.currentThread();
            if (state == 0 ){
                if (compareAndSetState(0,arg)){
//                    设置当前执行线程
                    setExclusiveOwnerThread(t);
                    return true;
                }
            }else if (t == getExclusiveOwnerThread()){
//                重入锁
                setState(state + 1);
                return true;
            }

            return false;

        }

        @Override
//        释放锁
        protected boolean tryRelease(int arg) {
//            锁的获取和释放肯定是一一对应的，所以调用此方法的一定为当前线程；
            Thread t = Thread.currentThread();
            if (t != getExclusiveOwnerThread()){
                throw new RuntimeException();
            }
            int state = getState()-arg;

            boolean flag = false;

            if (state == 0 ){
//                设置当前线程为空
                setExclusiveOwnerThread(null);
                flag = true;
            }

            setState(state);
//            重入锁为false
            return flag;

        }


        @Override
        protected int tryAcquireShared(int arg) {
            return super.tryAcquireShared(arg);
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            return super.tryReleaseShared(arg);
        }

        @Override
        protected boolean isHeldExclusively() {
            return super.isHeldExclusively();
        }
    }

}
