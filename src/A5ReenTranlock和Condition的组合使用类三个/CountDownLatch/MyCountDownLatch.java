package A5ReenTranlock和Condition的组合使用类三个.CountDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: cpb
 * @Date: 2019/2/13 14:43
 * @Description:
 */
public class MyCountDownLatch {
    final static CountDownLatch countDownLatch = new CountDownLatch(2);
    public static void main(String[] args) {
        for(int i = 0; i<6;i++) {
            Thread thread = new Thread(new Thread1());
            thread.start();
        }
    }

    static class Thread1 implements Runnable{
        @Override
        public void run() {
            System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
            try {
                countDownLatch.countDown();
                Thread.sleep(500);
                System.out.println("线程到齐");
                System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
