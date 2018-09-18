package A2线程锁;

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
        test1();
//        test2();
    }

    public void test1 (){
        if(null == threadLocal.get()) {
            threadLocal.set(k);
            System.out.println(Thread.currentThread().getName() + " i " + i++);
            System.out.println(Thread.currentThread().getName() + " k " + k++);
        }else{
            k =(int) threadLocal.get();
            System.out.println(Thread.currentThread().getName() + " i " + i++);
            System.out.println(Thread.currentThread().getName() + " k " + k++);
        }
    }



    public static void main(String[] args) {
        MyThreadLocal myThreadLocal = new MyThreadLocal();
        for (int i = 0; i < 20;i++) {
            Thread thread = new Thread(myThreadLocal);
            thread.start();
        }
    }

}
