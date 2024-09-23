package cn.shihh.zerojob.core.model;

import cn.shihh.zerojob.core.enums.TypeClassEnum;
import lombok.Data;

/**
 * 任务事件
 * 定时任务、延时任务、单次任务等都通过事件传递，根据事件获取下次执行时间并生成{@link Job}
 * @author shihh
 * @since 2024/9/23
 */
@Data
public class JobEvent {

    /**
     * 事件id
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
     * 是否立即执行
     */
    private Boolean isImmediately;

    /**
     * 执行时间cron表达式，非立即执行情况下有效
     */
    private String cronExpression;

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
    private TypeClassEnum jobType;

    /**
     * 任务次数
     */
    private Integer jobCount;

    /**
     * 已执行次数
     */
    private Integer executedCount;
}
