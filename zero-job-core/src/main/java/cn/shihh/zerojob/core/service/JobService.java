package cn.shihh.zerojob.core.service;

import cn.hutool.core.util.ObjectUtil;
import cn.shihh.zerojob.core.model.Job;

import java.util.Collection;
import java.util.List;

/**
 * 时间轮抽象服务接口
 * @author shihh
 * @since  2024/09/22
 */
public interface JobService {

    /**
     * 获取任务
     * @param jobKey 任务key
     * @return 任务
     */
    Job getJob(String jobKey);

    /**
     * 保存任务
     * @param job 任务
     */
    void saveJob(Job job);

    /**
     * 调度任务，将任务加入时间轮中
     * @param jobs
     */
    void scheduleJob(Collection<Job> jobs);

    /**
     * 执行任务
     * @param jobKey
     */
    default void executeJob(String jobKey) {
        Job job = getJob(jobKey);
        if (ObjectUtil.isNotNull(job)) {
            executeJob(job);
        }
    }

    /**
     * 执行任务
     * @param job
     */
    void executeJob(Job job);

    /**
     * 获取任务组中的所有任务
     * @param jobGroup 任务组
     * @return 任务组中的所有任务
     */
    List<Job> listByJobGroup(String jobGroup);

    /**
     * 删除任务
     * @param jobKey 任务key
     */
    Boolean deleteJob(String jobKey);

    /**
     * 删除任务组中的所有任务
     * @param jobGroup 任务组
     */
    Boolean deleteJobByGroup(String jobGroup);

    /**
     * 获取时间轮服务
     * @return 时间轮服务
     */
    WheelService getWheelService();

    /**
     * 加载任务到时间轮中
     * @param job 任务
     */
    default void loadJobToWheel(Job job) {
        getWheelService().loadJobToWheel(job);
    }

    /**
     * 从时间轮中移除任务
     * @param jobId 任务ID
     */
    default void removeJobFromWheel(String jobId) {
        getWheelService().removeJobFromWheel(jobId);
    }

}
