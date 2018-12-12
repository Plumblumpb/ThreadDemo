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
        // 其表示当前node的后继节点对应的线程需要被唤醒
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



//        private Node addWaiter(Node mode) {
//            Node node = new Node(Thread.currentThread(), mode);
//            // Try the fast path of enq; backup to full enq on failure
//            Node pred = tail;
//            if (pred != null) {
//                node.prev = pred;
//                if (compareAndSetTail(pred, node)) {
//                    pred.next = node;
//                    return node;
//                }
//            }
// 仔细看看上面的代码，如果会到这里，
// 说明 pred==null(队列是空的) 或者 CAS失败(有线程在竞争入队)
//            enq(node);【自旋的方式，死循环加入阻塞队列】
//            return node;
//        }




//        真正的线程挂起，然后被唤醒后去获取锁，都在这个方法里
//        final boolean acquireQueued(final Node node, int arg) {
//            boolean failed = true;
//            try {
//                boolean interrupted = false;
//                for (;;) {
//                      获取以前prev节点
//                    final Node p = node.predecessor();
        // p == head 说明当前节点虽然进到了阻塞队列，但是是阻塞队列的第一个，因为它的前驱是head
        // 注意，阻塞队列不包含head节点，head一般指的是占有锁的线程，head后面的才称为阻塞队列
        // 所以当前节点可以去试抢一下锁
        // 这里我们说一下，为什么可以去试试：
        // 首先，它是队头，这个是第一个条件，其次，当前的head有可能是刚刚初始化的node，
        // enq(node) 方法里面有提到，head是延时初始化的，而且new Node()的时候没有设置任何线程
        // 也就是说，当前的head不属于任何一个线程，所以作为队头，可以去试一试，
        // tryAcquire已经分析过了, 忘记了请往前看一下，就是简单用CAS试操作一下state
//                    if (p == head && tryAcquire(arg)) {
//                        setHead(node);
//                        p.next = null; // help GC
//                        failed = false;
//                        return interrupted;
//                    }
        // 到这里，说明上面的if分支没有成功，要么当前node本来就不是队头，
        // 要么就是tryAcquire(arg)没有抢赢别人，所以当前线程没有抢到锁，是否需要挂起当前线程
//                    if (shouldParkAfterFailedAcquire(p, node) &&
     // 这个方法很简单，因为前面返回true，所以需要挂起线程，这个方法就是负责挂起线程的
   // 这里用了LockSupport.park(this)来挂起线程，然后就停在这里了，等待被唤醒
//                            parkAndCheckInterrupt())
//                        interrupted = true;
//                }
//            } finally {
//                if (failed)
//                    cancelAcquire(node);
//            }
//        }



// 刚刚说过，会到这里就是没有抢到锁呗，这个方法说的是："当前线程没有抢到锁，是否需要挂起当前线程？"
// 第一个参数是前驱节点，第二个参数才是代表当前线程的节点
//private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
//    int ws = pred.waitStatus;
//    // 前驱节点的 waitStatus == -1 ，说明前驱节点状态正常，当前线程需要挂起，直接可以返回true
//    if (ws == Node.SIGNAL)
//        return true;
//
//    前驱节点 waitStatus大于0 ，之前说过，大于0 说明前驱节点取消了排队。这里需要知道这点：
//    进入阻塞队列排队的线程会被挂起，而唤醒的操作是由前驱节点完成的。
//    所以下面这块代码说的是将当前节点的prev指向waitStatus<=0的节点，
//    简单说，就是为了找个好爹，因为你还得依赖它来唤醒呢，如果前驱节点取消了排队，
//    找前驱节点的前驱节点做爹，往前循环总能找到一个好爹的
//    if (ws > 0) {
//        do {
//            node.prev = pred = pred.prev;
//        } while (pred.waitStatus > 0);
//        pred.next = node;
//    } else {
//        // 仔细想想，如果进入到这个分支意味着什么
//        // 前驱节点的waitStatus不等于-1和1，那也就是只可能是0，-2，-3
//        // 在我们前面的源码中，都没有看到有设置waitStatus的，所以每个新的node入队时，waitStatu都是0
//        // 用CAS将前驱节点的waitStatus设置为Node.SIGNAL(也就是-1)
//        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
//    }
//    return false;
//}

// 2. 接下来说说如果shouldParkAfterFailedAcquire(p, node)返回false的情况

// 仔细看shouldParkAfterFailedAcquire(p, node)，我们可以发现，其实第一次进来的时候，一般都不会返回true的，原因很简单，前驱节点的waitStatus=-1是依赖于后继节点设置的。也就是说，我都还没给前驱设置-1呢，怎么可能是true呢，但是要看到，这个方法是套在循环里的，所以第二次进来的时候状态就是-1了。

// 解释下为什么shouldParkAfterFailedAcquire(p, node)返回false的时候不直接挂起线程：
// => 是为了应对在经过这个方法后，node已经是head的直接后继节点了。剩下的读者自己想想吧。
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


    /**
     * 释放锁
      */
//    // 唤醒的代码还是比较简单的，你如果上面加锁的都看懂了，下面都不需要看就知道怎么回事了
//    public void unlock() {
//        sync.release(1);
//    }
//
//    public final boolean release(int arg) {
//        // 往后看吧
//        if (tryRelease(arg)) {
//            Node h = head;
//            if (h != null && h.waitStatus != 0)
//                unparkSuccessor(h);
//            return true;
//        }
//        return false;
//    }
//
//    // 回到ReentrantLock看tryRelease方法
//    protected final boolean tryRelease(int releases) {
//        int c = getState() - releases;
//        if (Thread.currentThread() != getExclusiveOwnerThread())
//            throw new IllegalMonitorStateException();
//        // 是否完全释放锁
//        boolean free = false;
//        // 其实就是重入的问题，如果c==0，也就是说没有嵌套锁了，可以释放了，否则还不能释放掉
//        if (c == 0) {
//            free = true;
//            setExclusiveOwnerThread(null);
//        }
//        setState(c);
//        return free;
//    }
//
//    /**
//     * Wakes up node's successor, if one exists.
//     *
//     * @param node the node
//     */
//// 唤醒后继节点
//// 从上面调用处知道，参数node是head头结点
//    private void unparkSuccessor(Node node) {
//        /*
//         * If status is negative (i.e., possibly needing signal) try
//         * to clear in anticipation of signalling.  It is OK if this
//         * fails or if status is changed by waiting thread.
//         */
//        int ws = node.waitStatus;
//        // 如果head节点当前waitStatus<0, 将其修改为0
//        if (ws < 0)
//            compareAndSetWaitStatus(node, ws, 0);
//        /*
//         * Thread to unpark is held in successor, which is normally
//         * just the next node.  But if cancelled or apparently null,
//         * traverse backwards from tail to find the actual
//         * non-cancelled successor.
//         */
//        // 下面的代码就是唤醒后继节点，但是有可能后继节点取消了等待（waitStatus==1）
//        // 从队尾往前找，找到waitStatus<=0的所有节点中排在最前面的
//        Node s = node.next;
//        if (s == null || s.waitStatus > 0) {
//            s = null;
//            // 从后往前找，仔细看代码，不必担心中间有节点取消(waitStatus==1)的情况
//            for (Node t = tail; t != null && t != node; t = t.prev)
//                if (t.waitStatus <= 0)
//                    s = t;
//        }
//        if (s != null)
//            // 唤醒线程
//            LockSupport.unpark(s.thread);
//    }


    /**
    *
    *唤醒线程以后，被唤醒的线程将从以下代码中继续往前走：
    */
//    private final boolean parkAndCheckInterrupt() {
//        LockSupport.park(this); // 刚刚线程被挂起在这里了
//        return Thread.interrupted();
//    }
}
