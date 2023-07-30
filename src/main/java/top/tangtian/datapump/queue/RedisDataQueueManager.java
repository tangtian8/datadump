package top.tangtian.datapump.queue;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.tangtian.datapump.dto.DataInfoDto;

/**
 * @author ：tian.tang
 * @description：RedisDataQueueManager
 * @date ：2023/07/04 10:42 AM
 */
@Service
public class RedisDataQueueManager {
    private final RedisTemplate<String,String> redisTemplate;
    private final Gson gson;

    @Autowired
    public RedisDataQueueManager(RedisTemplate<String, String> redisTemplate, Gson gson) {
        this.redisTemplate = redisTemplate;
        this.gson = gson;
    }


    private RedisTemplate<String,String> redisTemplate(){
        return this.redisTemplate;
    }

    public Long putData(final DataInfoDto data,final String model){
        BoundListOperations<String, String> opt =
                this.redisTemplate().boundListOps(dataSyncQueueKey() + model);

        String dataJson = this.gson.toJson(data);
        return  opt.rightPush(dataJson);
    }

    public DataInfoDto fetchData(final String model){
        BoundListOperations<String,String> opt =
                this.redisTemplate().boundListOps(dataSyncQueueKey() + model);
        String dataJson = (String)opt.leftPop();
        return (DataInfoDto)this.gson.fromJson(dataJson, DataInfoDto.class);
    }

    public String dataSyncQueueKey(){
        return "data:sync:queue:";
    }


}
