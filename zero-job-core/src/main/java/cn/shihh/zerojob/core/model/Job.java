package cn.shihh.zerojob.core.model;

import cn.shihh.zerojob.core.enums.JobStatus;
import cn.shihh.zerojob.core.enums.TypeClassEnum;
import lombok.Data;

import java.util.Date;

/**
 * 最小任务单元
 * @author shihh
 * @since  2024/9/22
 */
@Data
public class Job {

    /**
     * 任务唯一标识
     */
    private String jobKey;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务分组/批次，用于标识任务的业务范围，可以用eventId标识
     */
    private String jobGroup;

    /**
     * 任务执行时间
     */
    private Date jobExecuteTime;

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
     * 任务执行参数
     */
    private String jobParams;

    /**
     * 任务执行参数类型
     */
    private TypeClassEnum jobParamsType;

    /**
     * 任务状态
     */
    private JobStatus jobStatus;

}
