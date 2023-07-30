package top.tangtian.datapump.queue;

import top.tangtian.datapump.dto.DataInfoDto;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * @author ：tian.tang
 * @description：TODO
 * @date ：2023/07/04 10:49 AM
 */
public class ArrayQueue implements Queue<DataInfoDto>{
    private final ArrayBlockingQueue<DataInfoDto> sendQueue;

    private volatile CountDownLatch queueLatch;

    public ArrayQueue(ArrayBlockingQueue<DataInfoDto> sendQueue) {
        this.sendQueue = sendQueue;
    }

    @Override
    public DataInfoDto getData() {
        return this.sendQueue.poll();
    }

    @Override
    public void putData(DataInfoDto data) throws InterruptedException {
        this.sendQueue.put(data);
    }

    @Override
    public void setQueueLatch(CountDownLatch countDownLatch) {
        this.queueLatch = countDownLatch;
    }

    @Override
    public CountDownLatch getQueueLatch() {
        return this.queueLatch;
    }
}
