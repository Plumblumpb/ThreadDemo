package A9自定义阻塞队列生产消费者;


/**
 * @Auther: cpb
 * @Date: 2018/9/18 16:38
 * @Description:
 */
public class Demo {
    public static void main(String[] args) {
        MyCondition task = new MyCondition(5);

        Thread t1=new Thread(new Run1(task));
        Thread t2 = new Thread(new Run1(task));
        Thread t3=new Thread(new Run1(task));
        Thread t4 = new Thread(new Run1(task));
        Thread t5 = new Thread(new Run1(task));
        Thread t6 = new Thread(new Run1(task));
        Thread t7=new Thread(new Run2(task));
        Thread t8 = new Thread(new Run1(task));
        Thread t9=new Thread(new Run2(task));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();
        t8.start();
        t9.start();
    }
}
