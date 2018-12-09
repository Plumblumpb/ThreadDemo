package A7线程连接池;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: cpb
 * @Date: 2018/7/19 10:02
 * @Description: 自定义ThreadFactory，命名线程名
 */
public class MyThreadFactory implements ThreadFactory {
    //线程池数目
    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    //线程组，线程属于不同的线程组，相当于将线程归类
    private  ThreadGroup group ;
    //线程序号
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    //线程名前缀用于区分线程
    private  String namePreFix ;

    MyThreadFactory(){
        SecurityManager s = System.getSecurityManager();
        group = (s!=null)?s.getThreadGroup():Thread.currentThread().getThreadGroup();
        namePreFix = "myThread - "+POOL_NUMBER.getAndIncrement()+"-thread";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group,r,namePreFix+threadNumber.getAndIncrement());
        if(t.isDaemon()){
            t.setDaemon(false);
        }
        if(t.getPriority()!=Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
