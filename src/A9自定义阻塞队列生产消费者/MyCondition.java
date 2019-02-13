package A9自定义阻塞队列生产消费者;



import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: cpb
 * @Date: 2019/2/13 10:47
 * @Description:
 */
public class MyCondition {

    private Lock lock = new ReentrantLock();
    //满condition
    private Condition fullCondition = lock.newCondition();
    //为空Condition
    private Condition emptyCondition = lock.newCondition();
    //设置队列大小
    private Queue list = new LinkedList();
    //阻塞队列长度
    private int length;

    public MyCondition(int length){
        this.length = length;
    }

    /**
     * 生产者
     */
    public void add(Object o){
        try {
            lock.lock();
            //判断队列是否已经满了，队列阻塞，并等待唤醒
            while(length == list.size()){
                    fullCondition.await();
            }
            //否则加入队列
            list.add(o);
            System.out.println("The Lists Size is " + list.size());
            System.out.println("线程名加 is " + Thread.currentThread().getName());
            //唤醒其他线程，队列不为空了
            emptyCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费者
     */
    public void sub(){
        try {
            lock.lock();
            //队列为空进入阻塞状态
            if(list.size() == 0)
                emptyCondition.await();
            //否则弹出元素，并通知队列不为空。
            list.poll();
            fullCondition.signal();
            System.out.println("The Lists Size is " + list.size());
            System.out.println("线程名减 is " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

class Run1 implements Runnable{
    private MyCondition task;

    Run1(MyCondition task) {
        this.task = task;
    }

    @Override
    public void run() {
        task.add(1);
    }
}

class Run2 implements Runnable{
    private MyCondition task;

    Run2(MyCondition task) {
        this.task = task;
    }

    @Override
    public void run() {
        task.sub();
    }
}
