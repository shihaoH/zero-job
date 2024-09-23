package cn.shihh.zerojob.core.service;

import cn.shihh.zerojob.core.model.Job;

/**
 * 时间轮抽象服务接口
 * @author shihh
 * @since  2024/09/22
 */
public interface JobService {

    /**
     * 保存任务
     * @param job 任务
     */
    void saveJob(Job job);

    /**
     * 删除任务
     * @param jobId 任务ID
     */
    void deleteJob(String jobId);

    /**
     * 删除任务组中的所有任务
     * @param groupId 任务组ID
     */
    void deleteJobByGroupId(String groupId);

    /**
     * 加载任务到时间轮中
     * @param job 任务
     */
    void loadJobToWheel(Job job);

    /**
     * 从时间轮中移除任务
     * @param jobId 任务ID
     */
    void removeJobFromWheel(String jobId);

}
