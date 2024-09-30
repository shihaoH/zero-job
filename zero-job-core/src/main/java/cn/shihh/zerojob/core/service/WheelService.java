package cn.shihh.zerojob.core.service;

import cn.shihh.zerojob.core.model.Job;

/**
 * @author shihh
 * @since 2024/9/26
 */
public interface WheelService {

    /**
     * 将任务加载到时间轮中
     * @param job 任务
     */
    void loadJobToWheel(Job job);

    /**
     * 移除任务
     * @param jobKey 任务key
     */
    void removeJobFromWheel(String jobKey);

}
