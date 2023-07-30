package top.tangtian.datapump.service.impl;

import cn.hutool.core.util.ReUtil;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.tangtian.datapump.dao.DataSyncDao;
import top.tangtian.datapump.datamodel.annotation.TableName;
import top.tangtian.datapump.datamodel.db.DataSourceContextHolder;
import top.tangtian.datapump.datamodel.enums.MatchType;
import top.tangtian.datapump.datamodel.lock.IlockService;
import top.tangtian.datapump.datamodel.pojo.DataSync;
import top.tangtian.datapump.datamodel.util.AnnotationConfigStrategy;
import top.tangtian.datapump.handler.DataHandler;
import top.tangtian.datapump.model.DataInfo;
import top.tangtian.datapump.constants.OptType;
import top.tangtian.datapump.dto.DataInfoDto;
import top.tangtian.datapump.service.DataInfoBuilder;
import top.tangtian.datapump.service.DataSyncService;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ：tian.tang
 * @description：DataSyncServiceImpl
 * @date ：2023/07/05 10:38 AM
 */
@Service
@AllArgsConstructor
@Slf4j
public class DataSyncServiceImpl implements DataSyncService<DataInfoDto> {
    private final DataSyncDao dataSyncDao;
    private final DataInfoBuilder dataInfoBuilder;
    private final IlockService ilockService;
    @Override
    public List<DataInfoDto> findNeedSendData(final Integer maxLength) {
        List<DataInfoDto> resultList = Lists.newArrayList();
        String key = "findNeedSendData" + DataSourceContextHolder.getModel();
        String oldCurrentTime = ilockService.lock(key,10,20);
        try{
            int maxQueueSize = maxLength * 10;
            if (dataSyncDao.countUntreated() > maxQueueSize){
                return resultList;
            }
            log.info("查询需要处理数据-"+DataSourceContextHolder.getModel());
            List<DataSync> list = dataSyncDao.findNeedSendData(maxLength);
            list.forEach(res -> {
                DataInfoDto dataInfoDto = new DataInfoDto();
                BeanUtils.copyProperties(res,dataInfoDto);
                resultList.add(dataInfoDto);
            });
            if (!CollectionUtils.isEmpty(resultList)){
                List<Long> collect = resultList.stream().map(DataInfoDto::getId).
                        collect(Collectors.toList());
                this.lockDatas(collect);
            }
        }catch (Exception e){
            log.warn(e.getLocalizedMessage());
        }finally {
            ilockService.unlock(key,oldCurrentTime);
        }

        return resultList;
    }

    @Override
    public void syncData(DataInfoDto data) {
        this.log.info("message ----" + data.getId());
        AnnotationConfigStrategy<TableName, DataHandler,DataInfoDto> strategy =
                new AnnotationConfigStrategy<>(TableName.class,
                "IDataHandler",(obj) -> obj instanceof DataHandler,(conf, obj) -> {
                    boolean flag = false;
                    MatchType type = conf.matchType();
                    if (MatchType.Exact.equals(type)){
                        flag = Objects.equals(conf.value(),obj.getTableName());
                    }else if (MatchType.PreFix.equals(type)){
                        flag = StringUtils.startsWith(obj.getTableName(),conf.value());
                    }else {
                        if (!MatchType.Expression.equals(type)){
                            throw new RuntimeException();
                        }
                        flag = ReUtil.isMatch(conf.value(),obj.getTableName());
                    }
                    return flag && Objects.equals(conf.action(),obj.getAction());
        });
        DataHandler sendProcessor = strategy.lookupProcessor(data);

        if (Objects.isNull(sendProcessor)){
            strategy = new AnnotationConfigStrategy<>(TableName.class,
                            "ICommonDataHandler",(obj) -> obj instanceof DataHandler,(conf, obj) -> {
                        return Objects.equals(conf.action(),"common")
                                && Objects.equals(conf.action(),obj.getAction());
                    });
            sendProcessor = strategy.lookupProcessor(data);
        }
        try {
            List<DataInfo> dataInfos = dataInfoBuilder.setDataInfoProperties(data);
            for (DataInfo dataInfo : dataInfos){
                OptType optType = dataInfo.getOptType();
                if (OptType.New.equals(optType)){
                    sendProcessor.create(dataInfo);
                }else if (OptType.Update.equals(optType)){
                    sendProcessor.update(dataInfo);
                }else {
                    if (!OptType.Delete.equals(optType)){
                        throw new RuntimeException();
                    }
                    sendProcessor.delete(dataInfo);
                }
            }
            this.processSucessed(data);
        }catch (Exception e){
            log.warn("error{}",e);
            this.processfail(data,e.getMessage());
        }

    }

    private void processfail(DataInfoDto data, String message) {
        DataSync sync = new DataSync();
        BeanUtils.copyProperties(data,sync);
        int count = data.getExecutionCount() + 1;
        Date curr = new Date();
        Double add = 3 * Math.pow(2.0D,count);
        Date nextExecDate = DateUtils.addMinutes(curr,add.intValue());
        sync.setExecutionCount(count);
        sync.setNextExecutionTime(nextExecDate);
        sync.setUpdateTime(curr);
        sync.setErrInfo(message);
        sync.setStat(1);
        Integer result =  dataSyncDao.updateByPrimary(sync);
    }

    private void processSucessed(DataInfoDto data) {
        dataSyncDao.deletebyPrimaryKey(data.getId());
    }

    @Override
    public void reviseData() {

    }

    private void lockDatas(List<Long> ids) {
        dataSyncDao.lockByIds(ids);
    }
}
