package cn.shihh.zerojob.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.model.JobEvent;
import cn.shihh.zerojob.core.service.EventService;
import cn.shihh.zerojob.core.service.JobService;
import cn.shihh.zerojob.pojo.JobEventParam;
import cn.shihh.zerojob.pojo.JobEventVO;
import cn.shihh.zerojob.pojo.JobVO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shihh
 * @since 2024/10/9
 */
@RestController
@RequestMapping("/api/zero-job")
@RequiredArgsConstructor
public class ZeroJobController {

    private final EventService eventService;
    private final JobService jobService;

    @Scheduled(cron = "0 55 23 * * ?")
    public void scanNextDayJobs() {
        eventService.getNextDayEventToPublish();
    }

    @PostMapping("/event")
    public Boolean sendJobEvent(@RequestBody JobEventParam param) {
        JobEvent jobEvent = BeanUtil.toBean(param, JobEvent.class);
        jobEvent.setJobExecutor(param.getJobExecutorType().getName());
        eventService.saveJobEvent(jobEvent);
        return true;
    }

    @GetMapping("/event/list")
    public List<JobEventVO> listJobEvent() {
        return BeanUtil.copyToList(eventService.getJobEvents(), JobEventVO.class);
    }

    @GetMapping("/job/{eventId}")
    public List<JobVO> getJobEventById(@PathVariable String eventId) {
        List<Job> jobs = jobService.listByJobGroup(eventId);
        return BeanUtil.copyToList(jobs, JobVO.class);
    }

    @DeleteMapping("/event/{eventId}")
    public Boolean cancelJobEvent(@PathVariable String eventId) {
        return eventService.cancelJobEvent(eventId);
    }

    @DeleteMapping("/job/{jobKey}")
    public Boolean cancelJob(@PathVariable String jobKey) {
        return jobService.deleteJob(jobKey);
    }

}
