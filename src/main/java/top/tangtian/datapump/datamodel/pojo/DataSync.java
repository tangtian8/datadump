package top.tangtian.datapump.datamodel.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "data_sync")
public class DataSync {
    /**
     * 主键
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * key
     */
    private String keyName;

    /**
     * value
     */
    private String keyValue;

    /**
     * 操作类型
     */
    private String optType;

    /**
     * 执行动作
     */
    private String action;

    /**
     * 执行次数
     */
    private Integer executionCount;

    /**
     * 下次执行时间
     */
    private Date nextExecutionTime;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * redis的key组成表达式
     */
    private String keyExpression;

    /**
     * 错误信息
     */
    private String errInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态
     */
    private Integer stat;

    /**
     * 事件数据
     */
    private String eventData;

    /**
     * 历史变动数据
     */
    private String oldData;

    /**
     * 数据消费者
     */
    private String consumer;
}
