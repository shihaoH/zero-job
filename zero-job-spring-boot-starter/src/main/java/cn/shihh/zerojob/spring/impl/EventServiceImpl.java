package cn.shihh.zerojob.spring.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.shihh.zerojob.core.enums.JobTypeEnum;
import cn.shihh.zerojob.core.model.JobEvent;
import cn.shihh.zerojob.core.service.EventService;
import cn.shihh.zerojob.core.service.JobService;
import cn.shihh.zerojob.spring.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

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
        eventMapper.insert(jobEvent);
        if (ObjectUtil.isNull(jobEvent.getStartTime()) || DateUtil.date().isAfter(jobEvent.getStartTime())) {
            this.publishJobByEvent(jobEvent, DateUtil.date());
        }
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
