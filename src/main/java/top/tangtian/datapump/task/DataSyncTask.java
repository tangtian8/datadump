package top.tangtian.datapump.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.tangtian.datapump.config.SystemConfig;
import top.tangtian.datapump.datamodel.db.DataSourceContextHolder;
import top.tangtian.datapump.dto.DataInfoDto;
import top.tangtian.datapump.model.DataInfo;
import top.tangtian.datapump.queue.RedisDataQueueManager;
import top.tangtian.datapump.service.DataSyncService;

import java.util.List;
import java.util.TimerTask;

/**
 * @author ：tian.tang
 * @description：DataSyncTask
 * @date ：2023/07/12 7:01 PM
 */
@Slf4j
@Component
public class DataSyncTask extends TimerTask {
    @Autowired
    private DataSyncService dataSyncService;

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    RedisDataQueueManager redisDataQueueManager;

    @Override
    public void run() {
      while (true){
          try {
              this.send();
          }catch (Exception e){
              log.error("error->{}",e);
          }finally {
              try {
                  Thread.sleep(2000L);
              }catch (InterruptedException e){
                  log.error("InterruptedException->{}",e);
              }
          }
          log.info("task end");
      }
    }

    public void send(){
        List<DataInfoDto> needSendData = dataSyncService.findNeedSendData(2000);
        if (!CollectionUtils.isEmpty(needSendData)){
            log.info("处理数据总数： " + needSendData.size());
            process(needSendData);
        }
    }

    public void process(List<DataInfoDto> infos){
        if (false){

        }else {
            String model = DataSourceContextHolder.getModel();
            infos.forEach(e -> {
                redisDataQueueManager.putData(e,model);
            });
        }
    }
}
