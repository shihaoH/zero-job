package cn.shihh.zerojob.core.service;

import cn.hutool.core.util.ObjectUtil;
import cn.shihh.zerojob.core.enums.JobTypeEnum;
import cn.shihh.zerojob.core.model.JobEvent;

import java.util.Date;
import java.util.List;

/**
 * 事件抽象接口
 * @author shihh
 * @since 2024/9/23
 */
public interface EventService {

    /**
     * 发布任务事件
     * @param jobEvent 任务事件
     */
    void saveJobEvent(JobEvent jobEvent);

    /**
     * 获取所有任务事件
     * @return 所有任务事件
     */
    List<JobEvent> getJobEvents();

    /**
     * 根据事件ID取消任务事件
     * @param eventId 事件ID
     * @return 是否取消成功
     */
    Boolean cancelJobEvent(String eventId);

    /**
     * 根据任务事件发布任务
     * @param jobEvent 任务事件
     */
    default void publishJobByEvent(JobEvent jobEvent, Date jobStartTime) {
        JobTypeEnum jobType = jobEvent.getJobType();
        if (ObjectUtil.equal(jobType, JobTypeEnum.ONE_TIME)) {
            getJobService().saveJob(jobEvent.getJob());
        } else {
            getJobService().scheduleJob(jobEvent.getSchedulerJobs(jobStartTime));
        }
    }

    /**
     * 获取下一天需要发布的任务事件
     * 一般作为项目定时任务，每天定时检查是否有需要发布的任务事件
     */
    void getNextDayEventToPublish();

    /**
     * 获取任务服务
     * @return 任务服务
     */
    JobService getJobService();

}
