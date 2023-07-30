package top.tangtian.datapump;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import top.tangtian.datapump.config.SystemConfig;
import top.tangtian.datapump.datamodel.db.DataSourceContextHolder;
import top.tangtian.datapump.task.DataConsumerTask;
import top.tangtian.datapump.task.DataSyncTask;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：tian.tang
 * @description：start
 * @date ：2023/07/13 6:40 PM
 */
@Slf4j
public class DataPumpStarter {
    public static void start(ApplicationContext context,int mum, String dbName) {
        DataSourceContextHolder.setDataSource(dbName);
        DataSyncTask syncTask = context.getBean(DataSyncTask.class);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(syncTask);
        log.info(dbName + "数据同步生产线程 启动");

        SystemConfig config = context.getBean(SystemConfig.class);
        if (Objects.equals(config.model(),"default-queue")){
            DataConsumerTask consumerTask = context.getBean(DataConsumerTask.class);
            for(int i = 0; i < mum ;i++){
                executorService.execute(consumerTask);
                log.info(dbName + "数据同步消费线程 启动");
            }
        }
    }
}
