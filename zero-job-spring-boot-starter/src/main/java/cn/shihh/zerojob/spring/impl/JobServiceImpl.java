package cn.shihh.zerojob.spring.impl;

import cn.hutool.core.collection.CollUtil;
import cn.shihh.zerojob.core.enums.JobStatus;
import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.service.ExecutorService;
import cn.shihh.zerojob.core.service.JobService;
import cn.shihh.zerojob.core.service.WheelService;
import cn.shihh.zerojob.spring.mapper.JobMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final WheelService wheelService;
    private final JobMapper jobMapper;

    private final ApplicationContext applicationContext;

    @Override
    public Job getJob(String jobKey) {
        LambdaQueryWrapper<Job> qr = new LambdaQueryWrapper<>();
        qr.eq(Job::getJobKey, jobKey);
        qr.eq(Job::getJobStatus, JobStatus.RUNNING.getCode());
        return jobMapper.selectOne(qr);
    }

    @Override
    public void saveJob(Job job) {
        jobMapper.insert(job);
        Date jobExecuteTime = job.getJobExecuteTime();
        if (jobExecuteTime == null) {
            executeJob(job);
        } else {
            loadJobToWheel(job);
        }
    }

    @Override
    public void scheduleJob(Collection<Job> jobs) {
        if (CollUtil.isNotEmpty(jobs)) {
            jobs.forEach(this::saveJob);
        }
    }

    @Override
    public void executeJob(Job job) {
        ExecutorService executor = applicationContext.getBean(job.getJobExecutor(), ExecutorService.class);
        executor.executeJob(job);
    }

    @Override
    public void deleteJob(String jobKey) {
        LambdaQueryWrapper<Job> qr = new LambdaQueryWrapper<>();
        qr.eq(Job::getJobKey, jobKey);
        jobMapper.delete(qr);
    }

    @Override
    public void deleteJobByGroup(String jobGroup) {
        LambdaQueryWrapper<Job> qr = new LambdaQueryWrapper<>();
        qr.eq(Job::getJobGroup, jobGroup);
        jobMapper.delete(qr);
    }

    @Override
    public WheelService getWheelService() {
        return wheelService;
    }
}
