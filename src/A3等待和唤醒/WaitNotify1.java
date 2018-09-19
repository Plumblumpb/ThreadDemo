package A3等待和唤醒;

/**
 * @Auther: cpb
 * @Date: 2018/7/19 16:12
 * @Description:
 */
public class WaitNotify1 {


    public static void main(String[] args) {
        WaitNotify1 waitNotify1 = new WaitNotify1();
        Run1 run = new Run1();

        for(int j = 0; j< 2; j++){
            Thread thread = new Thread(run);
            thread.start();

        }

    }

    static final Object object = new Object();
    static int i = 1;
    static class Run1 implements Runnable{
        @Override
        public void run() {
            synchronized (object) {

                if(i == 0){
                    System.out.println("线程2 拿到了监视器锁。为什么呢，因为线程1 在 wait 方法的时候会自动释放锁");
                    System.out.println("线程2 执行 notify 操作");
                    object.notify();
                    System.out.println("线程2 执行完了 notify，先休息3秒再说。");
                    try {
                        Thread.sleep(3000);
                        System.out.println("线程2 休息完啦。注意了，调sleep方法和wait方法不一样，不会释放监视器锁");
                    } catch (InterruptedException e) {

                    }
                    System.out.println("线程2 休息够了，结束操作");
                }else {
                    System.out.println("线程1 获取到监视器锁");
                    try {
                        i = 0;
                        object.wait();
                        System.out.println("线程1 恢复啦。我为什么这么久才恢复，因为notify方法虽然早就发生了，可是我还要获取锁才能继续执行。");
                    } catch (InterruptedException e) {
                        System.out.println("线程1 wait方法抛出了InterruptedException异常");
                    }
                }
            }

        }
    }
}
