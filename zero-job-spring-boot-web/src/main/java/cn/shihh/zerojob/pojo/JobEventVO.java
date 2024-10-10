package cn.shihh.zerojob.pojo;

import cn.shihh.zerojob.core.enums.JobTypeEnum;
import cn.shihh.zerojob.core.enums.TypeClassEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author shihh
 * @since 2024/10/9
 */
@Data
public class JobEventVO {

    /**
     * 事件ID
     */
    private String eventId;

    /**
     * 事件名称
     */
    private String eventName;

    /**
     * 事件key
     */
    private String eventKey;

    /**
     * 执行时间cron表达式，非立即执行情况下有效
     */
    private String cron;

    /**
     * 任务参数
     */
    private String jobParams;

    /**
     * 任务参数类型
     */
    private TypeClassEnum jobParamsType;

    /**
     * 任务类型
     */
    private JobTypeEnum jobType;

    /**
     * 任务次数
     */
    private Integer jobCount;

    /**
     * 已执行次数
     */
    private Integer executedCount;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 任务执行者
     */
    private String jobExecutor;

    /**
     * 任务执行目标
     * 例如：http: https://target-service/targeturi
     *      脚本： /path/to/script.sh
     *      event: eventKey
     */
    private String jobExecuteTarget;

    /**
     * 事件创建时间
     */
    private Date createTime;
}
