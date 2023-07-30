package top.tangtian.datapump.handler;

import top.tangtian.datapump.model.DataInfo;

/**
 * @author ：tian.tang
 * @description：DataHandler
 * @date ：2023/07/04 10:01 AM
 */
public interface DataHandler {
    void create(final DataInfo dataInfo);

    void update(final DataInfo dataInfo);

    void delete(final DataInfo dataInfo);
}
