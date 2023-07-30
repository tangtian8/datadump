package top.tangtian.datapump.service;

import top.tangtian.datapump.dto.DataInfoDto;
import top.tangtian.datapump.model.DataInfo;

import java.util.List;

/**
 * @author ：tian.tang
 * @description：DataInfoBuilde
 * @date ：2023/07/05 9:23 AM
 */
public interface DataInfoBuilder {
    List<DataInfo> setDataInfoProperties(final DataInfoDto infoDto);
}
