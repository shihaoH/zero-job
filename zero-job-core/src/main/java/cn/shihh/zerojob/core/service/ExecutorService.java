package cn.shihh.zerojob.core.service;

import cn.shihh.zerojob.core.model.Job;

/**
 * @author shihh
 * @since 2024/9/26
 */
public interface ExecutorService {

    /**
     * 执行任务
     *
     * @param job 任务
     * @return 是否执行成功
     */
    Boolean executeJob(Job job);

}
