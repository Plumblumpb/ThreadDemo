package A5ReenTranlock和Condition的组合使用类三个.CountDownLatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Auther: cpb
 * @Date: 2019/2/13 14:43
 * @Description:
 */
public class MyCountDownLatch {

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        for(int i = 0; i<countDownLatch.getCount();i++) {
            Thread thread = new Thread(new Thread1(countDownLatch));
            thread.start();
        }
        try {
            System.out.println("正在等待所有玩家准备好");
            countDownLatch.await();
            System.out.println("开始游戏");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Thread1 implements Runnable{
        private CountDownLatch countDownLatch;
        public Thread1(CountDownLatch countDownLatch){
            this.countDownLatch = countDownLatch;
        }
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName()+" 已经准备好了");
                countDownLatch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
