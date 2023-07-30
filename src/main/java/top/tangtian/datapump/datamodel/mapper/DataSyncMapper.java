package top.tangtian.datapump.datamodel.mapper;


import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;
import top.tangtian.datapump.datamodel.pojo.DataSync;
@Repository
public interface DataSyncMapper extends Mapper<DataSync> {
}
