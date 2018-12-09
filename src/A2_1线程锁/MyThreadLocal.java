package A2_1线程锁;

/**
 * @Auther: cpb
 * @Date: 2018/9/17 18:47
 * @Description:
 */
public class MyThreadLocal implements Runnable{

    private static ThreadLocal threadLocal = new ThreadLocal();

    private int i = 0;
    private static  int j = 0;
    private int k = 0;

    public  void run() {
//        test1();
        test2();
    }

    public void test1 (){
        try {
            if(null == threadLocal.get()) {
                System.out.println(Thread.currentThread().getName() + " i " + i++);
                threadLocal.set(k);
                System.out.println(Thread.currentThread().getName() + " k " + k++);
                Thread.sleep(10);
            }else{
                System.out.println(Thread.currentThread().getName() + " i " + i++);
                k =(int) threadLocal.get();
                System.out.println(Thread.currentThread().getName() + " k " + k++);
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void test2 (){
        try {


                threadLocal.set(k);
                System.out.println(Thread.currentThread().getName() + " i " + i++);
                Thread.sleep(10);
                k = (int)threadLocal.get();
                System.out.println(Thread.currentThread().getName() + " k " +k ++);
                Thread.sleep(10);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        MyThreadLocal myThreadLocal = new MyThreadLocal();
        myThreadLocal.threadLocal.set(0);
        for (int i = 0; i < 20;i++) {
            Thread thread = new Thread(myThreadLocal);
            thread.start();
        }
    }

}
