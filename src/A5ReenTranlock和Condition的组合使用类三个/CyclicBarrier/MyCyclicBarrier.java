package A5ReenTranlock和Condition的组合使用类三个.CyclicBarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Auther: cpb
 * @Date: 2019/2/13 14:56
 * @Description:
 */
public class MyCyclicBarrier {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);
        for (int i = 0; i<cyclicBarrier.getParties(); i++) {
            Thread thread = new Thread(new Thread1(cyclicBarrier));
            thread.start();
        }
        System.out.println("ok");
    }

    static class Thread1 implements Runnable{
        private CyclicBarrier cyclicBarrier;
        public Thread1(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }
        @Override
        public void run() {
            for(int i = 0; i < 3; i++) {
                try {
                    System.out.println(Thread.currentThread().getName() + ", 通过了第"+i+"个障碍物");
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
