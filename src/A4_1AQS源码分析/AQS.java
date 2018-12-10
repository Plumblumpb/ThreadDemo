package A4_1AQS源码分析;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;

/**
 * @Auther: cpb
 * @Date: 2018/12/10 18:39
 * @Description:
 */
public class AQS {

    static final class Node {
        /** Marker to indicate a node is waiting in shared mode */
        // 标识节点当前在共享模式下
        static final Node SHARED = new Node();
        /** Marker to indicate a node is waiting in exclusive mode */
        // 标识节点当前在独占模式下
        static final Node EXCLUSIVE = null;

        // ======== 下面的几个int常量是给waitStatus用的 ===========
        /** waitStatus value to indicate thread has cancelled */
        // 代码此线程取消了争抢这个锁
        static final int CANCELLED =  1;
        /** waitStatus value to indicate successor's thread needs unparking */
        // 官方的描述是，其表示当前node的后继节点对应的线程需要被唤醒
        static final int SIGNAL    = -1;
        /** waitStatus value to indicate thread is waiting on condition */
        // 本文不分析condition，所以略过吧，下一篇文章会介绍这个
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         */
        // 同样的不分析，略过吧
        static final int PROPAGATE = -3;
        // 取值为上面的1、-1、-2、-3，或者0(以后会讲到)
        // 这么理解，暂时只需要知道如果这个值 大于0 代表此线程取消了等待，
        // 也许就是说半天抢不到锁，不抢了，ReentrantLock是可以指定timeouot的。。。
        volatile int waitStatus;
        // 前驱节点的引用
        volatile Node prev;
        // 后继节点的引用
        volatile Node next;
        // 这个就是线程本尊
        volatile Thread thread;
    }

    abstract static class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = -5179523762034025860L;
        /**
         * 锁（公平和非公平）
         */
        abstract void lock();

        /**
         * 非公平尝试获取锁（acquires = 1）
         */
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
//            状态0为无锁，1为有锁，大于1为重入锁。
            int c = getState();
            if (c == 0) {
//                CAS操作设置线程状态为1
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
//            判断是否为充入锁（重入state加1）
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) // overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
        /**
         * 尝试释放锁（releases = 1）
         */
        protected final boolean tryRelease(int releases) {
//            state-1
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread())
                throw new IllegalMonitorStateException();
            boolean free = false;
//            state为0时，释放锁，为无锁状态
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }

        /**
         *
         * 判断持有锁的线程是否为当前线程
         */
        protected final boolean isHeldExclusively() {
            // While we must in general read state before owner,
            // we don't need to do so to check if current thread is owner
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }

        // Methods relayed from outer class

        /**
         *
         * 获取持有锁的线程
         */
        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }

        /**
         *
         * 获取持有锁的线程的state
         */
        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }

        /**
         *
         * 判断被锁
         */
        final boolean isLocked() {
            return getState() != 0;
        }

        /**
         * Reconstitutes the instance from a stream (that is, deserializes it).
         * 反序列化
         */
        private void readObject(java.io.ObjectInputStream s)
                throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }

    /**
     * 非公平锁
     */
    static final class NonfairSync extends Sync {
        private static final long serialVersionUID = 7316153563782823691L;

        /**
         * 尝试获取锁
         */
        final void lock() {
//            乐观锁，尝试获取锁，成功则返回。
            if (compareAndSetState(0, 1))
                setExclusiveOwnerThread(Thread.currentThread());
            else
                acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }

    /**
     * 公平锁
     */
    static final class FairSync extends Sync {
        private static final long serialVersionUID = -3000897897090466540L;

        final void lock() {
//            if (!tryAcquire(arg)  【子类重写了父类的方法】&&
//              acquireQueued(addWaiter(Node.EXCLUSIVE), arg)  【独占模式下面加入阻塞队列中】)
//                selfInterrupt()
            acquire(1);
        }

        /**
         *
         */
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                //hasQueuedPredecessors(是否为head节点)   return head != tail &&((s = head.next) == null || s.thread != Thread.currentThread());
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }

}
