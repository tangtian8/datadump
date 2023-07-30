package top.tangtian.datapump.model;

import org.apache.commons.lang3.StringUtils;
import top.tangtian.datapump.constants.OptType;

import java.util.Date;
import java.util.Map;

public class DataInfo {
    private Long id;
    private String tableName;
    private String keyName;
    private String keyValue;
    private OptType optType;
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
    private  Map<String,Object> columnValues;
    private  Map<String,Object> properties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public OptType getOptType() {
        return optType;
    }

    public void setOptType(OptType optType) {
        this.optType = optType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public Integer getExecutionCount() {
        return executionCount;
    }

    public void setExecutionCount(Integer executionCount) {
        this.executionCount = executionCount;
    }

    public Date getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(Date nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getKeyExpression() {
        return keyExpression;
    }

    public void setKeyExpression(String keyExpression) {
        this.keyExpression = keyExpression;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public boolean isNotExist() {
        return isNotExist;
    }

    public void setNotExist(boolean notExist) {
        isNotExist = notExist;
    }

    public Map<String, Object> getColumnValues() {
        return columnValues;
    }

    public void setColumnValues(Map<String, Object> columnValues) {
        this.columnValues = columnValues;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Object setColumnValue(final String columnName, final Object value){
        return this.columnValues.put(StringUtils.lowerCase(columnName),value);
    }

}
