package top.tangtian.datapump.common;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory {
    private final String prefix;

    private volatile long counter;

    public MyThreadFactory(String prefix) {
        this.prefix = prefix;
    }


    public String prefix(){
        return this.prefix;
    }

    private long counter(){
        return this.counter;
    }



    private void counterEq(final  long x1){
        this.counter = x1;
    }

    @Override
    public Thread newThread(final Runnable r) {
        MyThread myThread = new MyThread(r,this.prefix() + "-" + this.counter());
        this.counterEq(this.counter + 1);
        myThread.setStartDate();
        return myThread;
    }
}
