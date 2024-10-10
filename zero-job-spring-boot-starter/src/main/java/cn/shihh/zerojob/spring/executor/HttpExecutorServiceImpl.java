package cn.shihh.zerojob.spring.executor;

import cn.hutool.http.HttpUtil;
import cn.shihh.zerojob.core.enums.JobStatus;
import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.service.ExecutorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Slf4j
@Service("httpExecutor")
@RequiredArgsConstructor
public class HttpExecutorServiceImpl implements ExecutorService {

    @Override
    public Boolean executeJob(Job job) {
        if (job != null) {
            String jobExecuteUrl = job.getJobExecuteTarget();
            String jobParams = job.getJobParams();
            try {
                HttpUtil.post(jobExecuteUrl, jobParams);
                job.setJobStatus(JobStatus.SUCCEED);
                return true;
            } catch (Exception e) {
                log.error("execute job failed, jobExecuteUrl:{}, jobParams:{}, error:{}", jobExecuteUrl, jobParams, e.getMessage());
                job.setJobStatus(JobStatus.FAILED);
                return false;
            }
        }
        return false;
    }
}
