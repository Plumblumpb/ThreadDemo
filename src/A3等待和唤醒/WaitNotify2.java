package A3等待和唤醒;

/**
 * @Auther: cpb
 * @Date: 2018/7/19 16:16
 * @Description:
 */
public class WaitNotify2 {


        public static void main(String[] args) {

            final Object object = new Object();

            final Thread thread1 = new Thread(new Runnable() {

                public void run() {

                    synchronized (object) {
                        System.out.println("线程1 获取到监视器锁");
                        try {
                            object.wait();
                            System.out.println("线程1 恢复啦。我为什么这么久才恢复，因为notify方法虽然早就发生了，可是我还要获取锁才能继续执行。");
                        } catch (InterruptedException e) {
                            System.out.println("线程1 wait方法抛出了InterruptedException异常，即使是异常，我也是要获取到监视器锁了才会抛出");
                        }
                    }
                }
            }, "线程1");
            thread1.start();

            new Thread(new Runnable() {

                public void run() {
                    synchronized (object) {
                        System.out.println("线程2 拿到了监视器锁。为什么呢，因为线程1 在 wait 方法的时候会自动释放锁");
                        System.out.println("线程2 设置线程1 中断");
                        thread1.interrupt();
                        System.out.println("线程2 执行完了 中断，先休息3秒再说。");
                        try {
                            Thread.sleep(3000);
                            System.out.println("线程2 休息完啦。注意了，调sleep方法和wait方法不一样，不会释放监视器锁");
                        } catch (InterruptedException e) {

                        }
                        System.out.println("线程2 休息够了，结束操作");
                    }
                }
            }, "线程2").start();
        }
    }

