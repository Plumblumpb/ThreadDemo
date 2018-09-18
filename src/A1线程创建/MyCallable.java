package A1线程创建;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Auther: cpb
 * @Date: 2018/9/17 17:04
 * @Description:
 */
public class MyCallable implements Callable {

    private int i = 0;
    private static  int j = 0;
    private int k = 0;

    @Override
    public Object call() throws Exception {
        System.out.println(Thread.currentThread().getName()+" "+i++);
        System.out.println(Thread.currentThread().getName()+" "+j++);
        System.out.println(Thread.currentThread().getName()+" "+k++);

        return j;
    }

    public static void main(String[] args) {
        MyCallable myCallable = new MyCallable();
        FutureTask futureTask = new FutureTask(myCallable);
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            System.out.println(futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
