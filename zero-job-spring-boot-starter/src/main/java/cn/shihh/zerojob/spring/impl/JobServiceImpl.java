package cn.shihh.zerojob.spring.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.shihh.zerojob.core.enums.JobStatus;
import cn.shihh.zerojob.core.enums.JobTypeEnum;
import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.model.JobEvent;
import cn.shihh.zerojob.core.service.ExecutorService;
import cn.shihh.zerojob.core.service.JobService;
import cn.shihh.zerojob.core.service.WheelService;
import cn.shihh.zerojob.spring.mapper.EventMapper;
import cn.shihh.zerojob.spring.mapper.JobMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final WheelService wheelService;
    private final JobMapper jobMapper;
    private final EventMapper eventMapper;

    private final ApplicationContext applicationContext;

    @Override
    public Job getJob(String jobKey) {
        LambdaQueryWrapper<Job> qr = new LambdaQueryWrapper<>();
        qr.eq(Job::getJobKey, jobKey);
        qr.eq(Job::getJobStatus, JobStatus.RUNNING);
        return jobMapper.selectOne(qr);
    }

    @Override
    public void saveJob(Job job) {
        job.setJobKey(IdWorker.get32UUID());
        jobMapper.insert(job);
        Date jobExecuteTime = job.getJobExecuteTime();
        // 如果执行时间早于当前时间，则立即执行
        if (jobExecuteTime == null || DateUtil.date().isAfterOrEquals(jobExecuteTime)) {
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
        try {
            jobExecuteRecord(job);
        } catch (Exception e) {
            log.error("任务执行记录失败", e);
        }
    }

    @Override
    public List<Job> listByJobGroup(String jobGroup) {
        LambdaQueryWrapper<Job> qr = new LambdaQueryWrapper<>();
        qr.eq(Job::getJobGroup, jobGroup);
        qr.orderByDesc(Job::getJobExecuteTime);
        return jobMapper.selectList(qr);
    }

    private void jobExecuteRecord(Job job) {
        LambdaUpdateWrapper<Job> up = new LambdaUpdateWrapper<>();
        up.eq(Job::getJobKey, job.getJobKey());
        up.set(Job::getJobStatus, ObjectUtil.equal(job.getJobStatus(), JobStatus.SUCCEED) ? JobStatus.SUCCEED : JobStatus.FAILED);
        jobMapper.update(up);
        // 记录执行次数
        LambdaQueryWrapper<JobEvent> qr = new LambdaQueryWrapper<>();
        qr.eq(JobEvent::getEventId, job.getJobGroup());
        JobEvent jobEvent = eventMapper.selectOne(qr);
        if (jobEvent != null) {
            eventMapper.addJobExecuteCount(job.getJobGroup());
            if (ObjectUtil.equal(jobEvent.getJobType(), JobTypeEnum.ONE_TIME) || ObjectUtil.equal(ObjectUtil.defaultIfNull(jobEvent.getExecutedCount(), 0) + 1, jobEvent.getJobCount())) {
                eventMapper.completeJobEvent(job.getJobGroup());
            }
        }
    }

    @Override
    public Boolean deleteJob(String jobKey) {
        LambdaUpdateWrapper<Job> up = new LambdaUpdateWrapper<>();
        up.eq(Job::getJobKey, jobKey);
        up.eq(Job::getJobStatus, JobStatus.RUNNING);
        up.set(Job::getJobStatus, JobStatus.CANCELLED);
        return jobMapper.update(up) > 0;
    }

    @Override
    public Boolean deleteJobByGroup(String jobGroup) {
        LambdaUpdateWrapper<Job> up = new LambdaUpdateWrapper<>();
        up.eq(Job::getJobGroup, jobGroup);
        up.eq(Job::getJobStatus, JobStatus.RUNNING);
        up.set(Job::getJobStatus, JobStatus.CANCELLED);
        return jobMapper.update(up) > 0;
    }

    @Override
    public WheelService getWheelService() {
        return wheelService;
    }
}
