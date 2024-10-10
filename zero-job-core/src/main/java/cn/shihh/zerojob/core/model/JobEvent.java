package cn.shihh.zerojob.core.model;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.shihh.zerojob.core.enums.EventStatus;
import cn.shihh.zerojob.core.enums.JobStatus;
import cn.shihh.zerojob.core.enums.JobTypeEnum;
import cn.shihh.zerojob.core.enums.TypeClassEnum;
import cn.shihh.zerojob.core.util.CronExpression;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
     * 任务创建时间
     */
    private Date createTime;

    /**
     * 事件状态
     */
    private EventStatus eventStatus;

    @SneakyThrows
    public List<Date> getExecuteTimes(Date executeStartTime) {
        DateTime executeEndTime = DateUtil.endOfDay(executeStartTime);
        if (ObjectUtil.equal(getJobType(), JobTypeEnum.ONE_TIME)) {
            if (ObjectUtil.isNull(getStartTime()) || executeEndTime.isAfterOrEquals(getStartTime())) {
                return Collections.singletonList(getStartTime());
            } else {
                return Collections.emptyList();
            }
        }
        DateTime nextTime = DateUtil.date(executeStartTime);
        List<Date> nextDayExecuteTimes = new ArrayList<>();
        CronExpression cronExpression = new CronExpression(this.getCron());
        nextTime = DateUtil.date(cronExpression.getNextValidTimeAfter(nextTime));
        while ((ObjectUtil.isNull(getStartTime()) || nextTime.isAfterOrEquals(getStartTime())) &&
                (ObjectUtil.isNull(getEndTime()) || nextTime.isBeforeOrEquals(getEndTime())) &&
                nextTime.isBeforeOrEquals(executeEndTime)) {
            nextDayExecuteTimes.add(nextTime);
            nextTime = DateUtil.date(cronExpression.getNextValidTimeAfter(nextTime));
        }

        return nextDayExecuteTimes;
    }

    public List<Job> getSchedulerJobs(Date executeStartTime) {
        List<Date> nextDayExecuteTimes = getExecuteTimes(executeStartTime);
        if (ObjectUtil.isEmpty(nextDayExecuteTimes)) {
            return Collections.emptyList();
        }
        List<Job> jobs = new ArrayList<>();
        for (Date executeTime : nextDayExecuteTimes) {
            Job job = new Job();
            job.setJobKey(getEventId() + "-" + executeTime.getTime());
            job.setJobName(getEventName() + "-" + executeTime.getTime());
            job.setJobGroup(getEventId());
            job.setJobExecuteTime(executeTime);
            job.setJobExecutor(getJobExecutor());
            job.setJobExecuteTarget(getJobExecuteTarget());
            job.setJobParams(getJobParams());
            job.setJobParamsType(getJobParamsType());
            job.setJobStatus(JobStatus.RUNNING);
            jobs.add(job);
        }
        return jobs;
    }

    public Job getJob() {
        Job job = new Job();
        long executeTime = ObjectUtil.defaultIfNull(getStartTime(), DateUtil.date()).getTime();
        job.setJobKey(getEventId() + "-" + executeTime);
        job.setJobName(getEventName());
        job.setJobGroup(getEventId());
        job.setJobExecutor(getJobExecutor());
        job.setJobExecuteTarget(getJobExecuteTarget());
        job.setJobParams(getJobParams());
        job.setJobParamsType(getJobParamsType());
        job.setJobExecuteTime(getStartTime());
        job.setJobStatus(JobStatus.RUNNING);
        return job;
    }
}
