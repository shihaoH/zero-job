package cn.shihh.zerojob.spring.executor;

import cn.shihh.zerojob.core.enums.JobStatus;
import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.service.ExecutorService;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Service("rocketMqExecutor")
@RequiredArgsConstructor
@Slf4j
public class RocketMqExecutorServiceImpl implements ExecutorService {

    private final RocketMQTemplate template;

    @Override
    public Boolean executeJob(Job job) {
        String mqDestination = job.getJobExecuteTarget();
        String jobParams = job.getJobParams();
        Message<Object> sendMessage = MessageBuilder.withPayload(job.getJobParamsType().converter(jobParams)).setHeader(RocketMQHeaders.KEYS, job.getJobKey()).build();
        try {
            SendResult sendResult = template.syncSend(mqDestination, sendMessage.getPayload());

            log.info("[{}]同步消息[{}]发送结果[{}]", mqDestination, sendMessage.getPayload(), JSONObject.toJSON(sendResult));

            job.setJobStatus(JobStatus.SUCCEED);
        } catch (Exception e) {
            job.setJobStatus(JobStatus.FAILED);
            log.error("[{}]同步消息[{}]发送失败", mqDestination, sendMessage.getPayload(), e);
        }
        return true;
    }
}
