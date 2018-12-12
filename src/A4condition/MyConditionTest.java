package A4condition;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Auther: cpb
 * @Date: 2018/9/18 15:24
 * @Description:
 */
public class MyConditionTest {
    private final Lock lock = new ReentrantLock();

    private final Condition addCondition = lock.newCondition();

    private final Condition subCondition = lock.newCondition();


    private static int num = 0;
    private List<String> lists = new LinkedList<String>();

    public void add() {
        lock.lock();

        try {
            while(lists.size() == 5) {//当集合已满,则"添加"线程等待
                System.out.println("当集合已满,则添加线程等待");
                addCondition.await();
            }

            num++;
            lists.add("add Banana" + num);
            System.out.println("The Lists Size is " + lists.size());
            System.out.println("线程名加 is " + Thread.currentThread().getName());
            System.out.println("==============================");
            this.subCondition.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {//释放锁
            lock.unlock();
        }
    }


    public void sub() {
        lock.lock();
        try {
            while(lists.size() == 0) {//当集合为空时,"减少"线程等待
                subCondition.await();
            }

            String str = lists.get(0);
            lists.remove(0);
            System.out.println("Banana数目减 is [" + str + "]");
            System.out.println("The Lists Size is " + lists.size());
            System.out.println("线程名减 is " + Thread.currentThread().getName());
            System.out.println("==============================");
            num--;
            addCondition.signal();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}

class Run1 implements Runnable{
    private MyConditionTest task;

    public Run1(MyConditionTest task) {
        this.task = task;
    }

    @Override
    public void run() {
        task.add();
    }
}

class Run2 implements Runnable{
    private MyConditionTest task;

    public Run2(MyConditionTest task) {
        this.task = task;
    }

    @Override
    public void run() {
        task.sub();
    }
}
