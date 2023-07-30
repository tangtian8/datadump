package top.tangtian.datapump.datamodel.db;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceContextHolder {
    //当前线程使用到数据源，为null表示默认数据源
    private static final ThreadLocal<DataSource> contextHolder = new InheritableThreadLocal<DataSource>();

    private static final ThreadLocal<String> modelHolder = new InheritableThreadLocal<String>();

    // 全局外部数据源缓存
    private static final Map<String,DataSource> map = new ConcurrentHashMap<String,DataSource>();

    public static String getModel(){
        return modelHolder.get();
    }

    public static void setDataSource(String key){
        contextHolder.set(map.get(key));
        modelHolder.set(key);
    }

    public static void addDataSource(String key,DataSource dataSource){
        map.put(key,dataSource);
    }

    public static DataSource getDataSource(){
        return contextHolder.get();
    }

    public static void clearCache(){
        map.clear();
    }

    public static void clearDataSource(){
        contextHolder.remove();
    }

}
