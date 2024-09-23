package cn.shihh.zerojob.core.service;

import cn.shihh.zerojob.core.model.JobEvent;

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
    void publishJobEvent(JobEvent jobEvent);

}
