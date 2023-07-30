package top.tangtian.datapump.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.tangtian.datapump.dao.JdbcDataInfoDao;
import top.tangtian.datapump.model.DataInfo;
import top.tangtian.datapump.exception.SystemException;
import top.tangtian.datapump.constants.OptType;
import top.tangtian.datapump.dto.DataInfoDto;
import top.tangtian.datapump.service.DataInfoBuilder;
import top.tangtian.datapump.util.MyBeanUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author ：tian.tang
 * @description：TODO
 * @date ：2023/07/05 9:25 AM
 */
@Service
public class DataInfoBuilderImpl implements DataInfoBuilder {
    private final JdbcDataInfoDao dataInfoDao;

    private final Logger logger;

    private JdbcDataInfoDao getDataInfoDao() {
        return dataInfoDao;
    }

    public DataInfoBuilderImpl(@Autowired final JdbcDataInfoDao dataInfoDao) {
        this.dataInfoDao = dataInfoDao;
        this.logger = LoggerFactory.getLogger(DataInfoBuilderImpl.class);
    }

    @Override
    public List<DataInfo> setDataInfoProperties(DataInfoDto infoDto) {
        List<DataInfo> result = Lists.newArrayList();
        List<Map<String, Object>> byDBSync = this.getDataInfoDao().findByDBSync(infoDto);

        if (CollectionUtils.isEmpty(byDBSync)
        && (Objects.isNull(infoDto.getOptType()) || Objects.equals(infoDto.getOptType(),
                OptType.Update.getActionName()) || Objects.equals(infoDto.getOptType(),
                OptType.New.getActionName()))){
            throw new SystemException();
        }else {
            if (!CollectionUtils.isEmpty(byDBSync) && !Objects.equals(infoDto.getOptType(),
                    OptType.Delete.getActionName())){
                byDBSync.forEach(infoMap -> {
                    DataInfo dataInfo = MyBeanUtils.createBeanTarget(infoDto,DataInfo.class);
                    String var6 = infoDto.getOptType();
                    if (OptType.New.getActionName().equals(var6)){
                        dataInfo.setOptType(OptType.New);
                    }else if (OptType.Update.getActionName().equals(var6)){
                        dataInfo.setOptType(OptType.Update);
                    }else {
                        if (!OptType.Delete.getActionName().equals(var6)){
                            throw new SystemException();
                        }
                        dataInfo.setOptType(OptType.Delete);
                    }
                    result.add(this.populateData(dataInfo,infoMap));
                });
            }else {
                DataInfo dataInfo = MyBeanUtils.createBeanTarget(infoDto,DataInfo.class);
                dataInfo.setOptType(OptType.Delete);
                result.add(this.populateDeteteDataInfo(dataInfo));
            }
        }

        return result;
    }

    private DataInfo populateDeteteDataInfo(DataInfo dataInfo) {
        dataInfo.setColumnValues(Maps.newHashMap());
        dataInfo.setColumnValue(dataInfo.getKeyName(), dataInfo.getKeyValue());
        return dataInfo;
    }

    private DataInfo populateData(DataInfo dataInfo, Map<String, Object> infoMap) {
        dataInfo.setColumnValues(Maps.newHashMap());
        infoMap.forEach((key,val) -> {
            dataInfo.setColumnValue(key,val);
        });
        return dataInfo;
    }
}
