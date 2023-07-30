package top.tangtian.datapump.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.entity.Example;
import top.tangtian.datapump.datamodel.mapper.DataSyncMapper;
import top.tangtian.datapump.datamodel.pojo.DataSync;

import java.util.Date;
import java.util.List;

/**
 * @description：TODO
 * @author     ：tian.tang
 * @date       ：2023/07/05 10:59 AM
 */
@Repository
@AllArgsConstructor
public class DataSyncDao {
    private final DataSyncMapper syncMapper;

    public int countUntreated(){
        DataSync sync = new DataSync();
        sync.setStat(0);
       return syncMapper.selectCount(sync);
    }

    public List<DataSync> findNeedSendData(Integer maxLength) {
        Example example = new Example(DataSync.class,true,true);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("stat",1);
        criteria.andLessThanOrEqualTo("nextExecutionTime",new Date());
//        example.setOrderByClause("order by priority desc limit " + maxLength);
        return syncMapper.selectByExample(example);
    }

    public void lockByIds(List<Long> ids) {
        Example example = new Example(DataSync.class,true,true);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",ids);
        DataSync dataSync = new DataSync();
        dataSync.setUpdateTime(new Date());
        dataSync.setStat(0);
        syncMapper.updateByExampleSelective(dataSync,example);
    }

    public void deletebyPrimaryKey(Long id) {
        DataSync sync = new DataSync();
        sync.setId(id);
        syncMapper.deleteByPrimaryKey(sync);
    }

    public Integer updateByPrimary(DataSync sync) {
        return syncMapper.updateByPrimaryKeySelective(sync);
    }
}
