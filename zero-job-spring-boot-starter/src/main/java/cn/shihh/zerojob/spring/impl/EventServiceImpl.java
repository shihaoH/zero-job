package cn.shihh.zerojob.spring.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.shihh.zerojob.core.enums.EventStatus;
import cn.shihh.zerojob.core.enums.JobTypeEnum;
import cn.shihh.zerojob.core.model.JobEvent;
import cn.shihh.zerojob.core.service.EventService;
import cn.shihh.zerojob.core.service.JobService;
import cn.shihh.zerojob.spring.mapper.EventMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventMapper eventMapper;
    private final JobService jobService;

    @Override
    public void saveJobEvent(JobEvent jobEvent) {
        if (ObjectUtil.isNull(jobEvent)) {
            throw new IllegalArgumentException("jobEvent cannot be null");
        }
        if (ObjectUtil.notEqual(jobEvent.getJobType(), JobTypeEnum.ONE_TIME) && !CronExpression.isValidExpression(jobEvent.getCron())) {
            throw new IllegalArgumentException("cron expression is invalid");
        }
        jobEvent.setEventId(IdWorker.get32UUID());
        eventMapper.insert(jobEvent);
        if (ObjectUtil.isNull(jobEvent.getStartTime()) || DateUtil.date().isAfterOrEquals(jobEvent.getStartTime())) {
            this.publishJobByEvent(jobEvent, DateUtil.date());
        }
    }

    @Override
    public List<JobEvent> getJobEvents() {
        LambdaQueryWrapper<JobEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(JobEvent::getCreateTime);
        return eventMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelJobEvent(String eventId) {
        jobService.deleteJobByGroup(eventId);
        LambdaUpdateWrapper<JobEvent> up = new LambdaUpdateWrapper<>();
        up.eq(JobEvent::getEventId, eventId);
        up.set(JobEvent::getEventStatus, EventStatus.CANCELED);
        return eventMapper.update(null, up) > 0;
    }

    @Override
    public JobService getJobService() {
        return jobService;
    }

    @Override
    public void getNextDayEventToPublish() {
        DateTime now = DateUtil.date();
        DateTime nextDayBegin = DateUtil.beginOfDay(DateUtil.offsetDay(now, 1));
        List<JobEvent> nextDayEvent = eventMapper.getNextDayEvent(nextDayBegin);
        if (CollUtil.isNotEmpty(nextDayEvent)) {
            for (JobEvent e : nextDayEvent) {
                publishJobByEvent(e, nextDayBegin);
            }
        }
    }
}
