package A5ReenTranlock和Condition的组合使用类三个.Semaphore;

import java.util.concurrent.Semaphore;

/**
 * @Auther: cpb
 * @Date: 2019/2/13 16:19
 * @Description:
 */
public class MySemaphore {
    public static void main(String[] args) {
        for(int i = 0 ; i<10; i++){
            Thread thread = new Thread(new Thread1());
            thread.start();
        }

    }
    final static Semaphore semaphore = new Semaphore(5);

    static class Thread1 implements Runnable{
        @Override
        public void run() {
            try {
                semaphore.acquire();
                //获取运行的线程权限。
                System.out.println(Thread.currentThread().getName()+":开始运行");
                System.out.println(semaphore.availablePermits()+"次数");
                Thread.sleep(1500);
                System.out.println(Thread.currentThread().getName()+":线程结束");
                //释放权限。
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
