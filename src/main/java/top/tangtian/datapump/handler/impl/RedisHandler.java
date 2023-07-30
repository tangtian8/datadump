package top.tangtian.datapump.handler.impl;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.tangtian.datapump.handler.DataHandler;
import top.tangtian.datapump.exception.SystemException;
import top.tangtian.datapump.model.DataInfo;

import java.util.Map;

/**
 * @author ：tian.tang
 * @description：redisHandler
 * @date ：2023/07/04 9:56 AM
 */
@Service
@AllArgsConstructor
public class RedisHandler implements DataHandler {
    private final RedisTemplate<String,String> redisTemplate;


    @Override
    public void create(final DataInfo dataInfo) {
        String key = StringUtils.isNotBlank(dataInfo.getKeyExpression()) ?
                this.fetchKey(dataInfo) : dataInfo.getTableName() + "." + dataInfo.getKeyValue();
        BoundHashOperations opt = this.redisTemplate.boundHashOps(key);
        Map prop = Maps.newHashMapWithExpectedSize(dataInfo.getProperties().size());
        dataInfo.getProperties().forEach((keyV,value) -> {
           if (value instanceof Boolean){
               prop.put(keyV,(Boolean)value ? "1":"0");
           }else {
               prop.put(keyV,String.valueOf(value));
           }
        });
        opt.putAll(prop);
    }

    private String fetchKey(DataInfo dataInfo) {
        String keyExpression = dataInfo.getKeyExpression();
        return keyExpression;
    }

    @Override
    public void update(final DataInfo dataInfo) {
        this.create(dataInfo);
    }

    @Override
    public void delete(final DataInfo dataInfo) {
        boolean ok = this.redisTemplate.delete(dataInfo.getKeyExpression());
        if (!ok){
            throw new SystemException();
        }
    }
}
