package cn.shihh.zerojob.spring.executor;

import cn.hutool.http.HttpUtil;
import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.service.ExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Service("httpExecutor")
@RequiredArgsConstructor
public class HttpExecutorServiceImpl implements ExecutorService {

    @Override
    public Boolean executeJob(Job job) {
        if (job != null) {
            String jobExecuteUrl = job.getJobExecuteTarget();
            String jobParams = job.getJobParams();
            HttpUtil.post(jobExecuteUrl, jobParams);
            return true;
        }
        return false;
    }
}
