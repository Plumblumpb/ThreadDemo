package A5ReenTranlock和Condition的组合使用类三个.CountDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: cpb
 * @Date: 2018/7/26 16:56
 * @Description:CountDownLatchDown的使用
 */
public class TestOneCountDown {
    static final CountDownLatch latch = new CountDownLatch(2);
    public static void main(String[] args) {
        TestOneCountDown test = new TestOneCountDown();
        for (int i = 0 ; i < 2; i++){
            Run1 run = new Run1();
            Thread thread = new Thread(run);
            thread.start();
        }

    }


    static class Run1 implements Runnable{
    @Override
    public void run() {
//        try {
            System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
//            await()方法不执行也可以运行，但是会使后面的交替打印。

//            Thread.sleep(3000);
            System.out.println("线程到齐");
            System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
            latch.countDown();
//            latch.await();

//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
}
