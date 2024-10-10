package cn.shihh.zerojob.pojo;

import cn.shihh.zerojob.core.enums.JobTypeEnum;
import cn.shihh.zerojob.core.enums.TypeClassEnum;
import cn.shihh.zerojob.spring.executor.ExecutorTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author shihh
 * @since 2024/10/9
 */
@Data
public class JobEventParam {

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
     * 开始时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 任务执行者
     */
    private ExecutorTypeEnum jobExecutorType;

    /**
     * 任务执行目标
     * 例如：http: https://target-service/targeturi
     *      脚本： /path/to/script.sh
     *      event: eventKey
     */
    private String jobExecuteTarget;
}
