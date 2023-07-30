package top.tangtian.datapump.datamodel.lock;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockServiceImpl implements IlockService{

    private final Logger log = LoggerFactory.getLogger(RedisLockServiceImpl.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String KEY_PRE = "dk:";



    @Override
    public int queryStock(String code) {
        return Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(code)));
    }

    @Override
    public String lock(String lockKey, int timeout, int expire) {
        Map<String,String> result = lock(lockKey,timeout * 1000l,expire * 1000l);
        if (!isSuccessful(result)){
            throw new RuntimeException("未能获取到锁");
        }
        return result.get("lockTime");
    }

    private boolean isSuccessful(Map<String,String> result){
        return Objects.nonNull(result) && Objects.equals("true",result.get("lock"));
    }

    @Override
    public Map<String, String> lock(String lockKey, long timeout, long expire) {
        Map<String,String> map = null;
        String key = KEY_PRE + lockKey;
        long start = System.currentTimeMillis();
        try {
            map = Maps.newHashMap();
            while (System.currentTimeMillis() - start <= timeout){
                String oldTime = String.valueOf(System.currentTimeMillis() + expire);
                BoundValueOperations<String,String> opt = redisTemplate.boundValueOps(key);
                boolean setnx = opt.setIfAbsent(oldTime);
                if (setnx){
                    log.debug("{}加锁成功 key ---{}",Thread.currentThread().getName(),lockKey);
                    opt.expire(expire, TimeUnit.MICROSECONDS);
                    map.put("lock","true");
                    map.put("lockTime",oldTime);
                    return map;
                }else {
                    //加锁失败 可能是持有锁的进程宕机了并且没有设置失效时间 导致锁一致被暂用
                    String lockValue = opt.get();
                    if (StringUtils.isNotBlank(lockValue) &&
                    Objects.nonNull(lockValue) &&
                            Long.parseLong(lockValue) < System.currentTimeMillis()){
                        //锁过期 但是持有者错误，导致未设置过期时间
                        String oldExpireTime = opt.getAndSet(oldTime);
                        if (StringUtils.isNotBlank(oldExpireTime) && oldExpireTime.equals(lockValue)){
                            opt.expire(expire,TimeUnit.MICROSECONDS);//设置过期时间，在宕机时候，锁会是释放
                            log.info("{} 获得超时锁加锁成功",Thread.currentThread().getName());
                            map.put("lock","true");
                            map.put("lockTime",oldTime);
                            return map;
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("{}",e.getMessage());
        }
        return null;
    }

    @Override
    public void unlock(String key, String oldCurrentTime) {
        String lockKey = KEY_PRE + key;
        try {
            BoundValueOperations<String, String> operations =
                    redisTemplate.boundValueOps(lockKey);
            String redisOldCurrentTime = operations.get();
            if (StringUtils.isNotBlank(redisOldCurrentTime) &&
            redisOldCurrentTime.equals(oldCurrentTime)){
                redisTemplate.delete(lockKey);
                log.debug("{}有权限解锁，释放锁key--{}",Thread.currentThread().getName(),lockKey);
            }
        }catch (Exception e){
            log.error("{}",e);
        }
    }
}
