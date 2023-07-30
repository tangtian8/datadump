package top.tangtian.datapump.service;

import java.util.List;

/**
 * @author ：tian.tang
 * @description：DataSyncService
 * @date ：2023/07/05 10:36 AM
 */
public interface DataSyncService<T> {
    List<T> findNeedSendData(final Integer maxLength);

    void syncData(final T data);

    void reviseData();
}
