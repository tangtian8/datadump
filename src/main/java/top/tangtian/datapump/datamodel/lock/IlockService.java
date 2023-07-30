package top.tangtian.datapump.datamodel.lock;

import java.util.Map;

public interface IlockService {

    int queryStock(String code);

    /**
     * 加锁
     * @param lockKey 锁的键
     * @param timeout 等待锁的超时时间， 秒
     * @param expire 等待说的国旗时间， 秒
     * @return
     */
    String lock(String lockKey,int timeout,int expire);

    Map<String,String> lock(String lockKey,long timeout,long pexpire);

    void unlock(String key,String oldCurrentTime);




}
