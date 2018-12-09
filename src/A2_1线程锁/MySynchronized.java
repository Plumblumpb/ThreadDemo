package A2_1线程锁;

/**
 * @Auther: cpb
 * @Date: 2018/9/17 17:11
 * @Description:
 */
public class MySynchronized implements Runnable{
    private int i = 0;
    private static  int j = 0;
    private int k = 0;

    public  void run() {
//        test1();
        test2();
    }

    public synchronized void test1 (){
        System.out.println(Thread.currentThread().getName()+" i "+i++);
        System.out.println(Thread.currentThread().getName()+" j "+j++);
        System.out.println(Thread.currentThread().getName()+" k "+k++);
    }

    public void test2(){
        try {
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" i "+i++);
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+" j "+j++);
            synchronized((Integer)k){
                Thread.sleep(500);
                System.out.println(Thread.currentThread().getName()+" k "+k++);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        MySynchronized mySynchronized = new MySynchronized();

        for (int i = 0 ;i < 30 ; i++) {
            Thread thread = new Thread(mySynchronized);
            thread.start();
        }


    }
}
