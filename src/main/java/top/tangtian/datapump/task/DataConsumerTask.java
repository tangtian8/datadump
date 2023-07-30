package top.tangtian.datapump.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.tangtian.datapump.datamodel.db.DataSourceContextHolder;
import top.tangtian.datapump.dto.DataInfoDto;
import top.tangtian.datapump.queue.RedisDataQueueManager;
import top.tangtian.datapump.service.DataSyncService;

import java.util.Objects;

/**
 * @author ：tian.tang
 * @description：DataConsumerThread
 * @date ：2023/07/12 6:51 PM
 */
@Service
@AllArgsConstructor
@Slf4j
public class DataConsumerTask implements Runnable{
    private final DataSyncService dataSyncService;

    private final RedisDataQueueManager dataQueueManager;

    public void sendData(){
        log.info("消费线程心跳{}",Thread.currentThread().getName());
        String model = DataSourceContextHolder.getModel();
        DataInfoDto dataInfoDto =  dataQueueManager.fetchData(model);
        if (Objects.isNull(dataInfoDto)){

        }else {
            dataSyncService.syncData(dataInfoDto);
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                this.sendData();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    Thread.sleep(2000L);
                }catch (InterruptedException e){
                    log.error("InterruptedException->{}",e);
                }
            }
        }
    }
}
