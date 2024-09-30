package cn.shihh.zerojob.spring.executor;

import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.service.ExecutorService;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RocketMqExecutorServiceImpl implements ExecutorService {

    private final RocketMQTemplate template;

    @Override
    public Boolean executeJob(Job job) {
        String mqDestination = job.getJobExecuteTarget();
        String jobParams = job.getJobParams();
        Object o = job.getJobParamsType().converter(jobParams);
        SendResult sendResult = template.syncSend(mqDestination, o);

        // 此处为了方便查看给日志转了json，根据选择选择日志记录方式，例如ELK采集
        log.info("[{}]同步消息[{}]发送结果[{}]", mqDestination, o, JSONObject.toJSON(sendResult));
        return true;
    }
}
