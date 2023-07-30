package top.tangtian.datapump.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author ：tian.tang
 * @description：DataInfoDto
 * @date ：2023/07/04 9:35 AM
 */
@Data
public class DataInfoDto {
    private Long id;
    private String tableName;
    private String keyName;
    private String keyValue;
    private String optType;
    private String action;
    private String errInfo;
    private Integer executionCount;
    private Date nextExecutionTime;
    private Integer priority;
    private String keyExpression;
    private String eventData;
    private String oldData;
    private String consumer;
    private boolean isNotExist;
}
