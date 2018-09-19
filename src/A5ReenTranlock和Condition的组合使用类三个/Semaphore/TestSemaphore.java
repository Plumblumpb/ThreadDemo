package A5ReenTranlock和Condition的组合使用类三个.Semaphore;

import java.util.concurrent.Semaphore;

/**
 * @Auther: cpb
 * @Date: 2018/7/27 10:39
 * @Description:
 */
public class TestSemaphore {
    public static void main(String[] args) {
        int N = 8;            //工人数

        for(int i=0;i<N;i++)
            new Worker(i).start();
    }

    static class Worker extends Thread{
        private int num;
        private static Semaphore semaphore = new Semaphore(5); //机器数目
        public Worker(int num){
            this.num = num;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire();
                System.out.println("工人"+this.num+"占用一个机器在生产...");
                Thread.sleep(200);
                System.out.println(semaphore.availablePermits()+"次数");
                Thread.sleep(200);
                System.out.println("工人"+this.num+"释放出机器");
                Thread.sleep(200);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
