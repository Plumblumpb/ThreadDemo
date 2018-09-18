package A1线程创建;

/**
 * @Auther: cpb
 * @Date: 2018/9/17 16:53
 * @Description:
 */
public class MyThread extends Thread {

    private int i = 0;
    private static  int j = 0;
    private int k = 0;

    public void run() {
        System.out.println(Thread.currentThread().getName()+" "+i++);
        System.out.println(Thread.currentThread().getName()+" "+j++);

        System.out.println(Thread.currentThread().getName()+" "+k++);
    }

    public static void main(String[] args) {
        for (int i = 0 ;i < 30 ; i++){
//            创建线程
            MyThread myThread = new MyThread();
            myThread.start();
        }
    }
}
