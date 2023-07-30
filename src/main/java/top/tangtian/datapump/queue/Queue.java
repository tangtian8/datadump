package top.tangtian.datapump.queue;

import java.util.concurrent.CountDownLatch;

/**
 * @author ：tian.tang
 * @description：TODO
 * @date ：2023/07/04 10:44 AM
 */
public interface Queue<T> {
    T getData();

    void putData(final T data) throws InterruptedException;

    void setQueueLatch(final CountDownLatch countDownLatch);

    CountDownLatch getQueueLatch();
}
