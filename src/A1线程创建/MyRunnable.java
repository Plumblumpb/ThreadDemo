package A1线程创建;

/**
 * @Auther: cpb
 * @Date: 2018/9/17 17:01
 * @Description:
 */
public class MyRunnable implements Runnable {
    private int i = 0;
    private static  int j = 0;
    private int k = 0;

    public void run() {
        System.out.println(Thread.currentThread().getName()+" "+i++);
        System.out.println(Thread.currentThread().getName()+" "+j++);

        System.out.println(Thread.currentThread().getName()+" "+k++);
    }


    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        for (int i = 0 ;i < 30 ; i++) {

            Thread thread = new Thread(myRunnable);
            thread.start();
        }


    }
}
