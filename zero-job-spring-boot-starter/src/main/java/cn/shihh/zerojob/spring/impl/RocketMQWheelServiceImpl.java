package cn.shihh.zerojob.spring.impl;

import cn.shihh.zerojob.core.model.Job;
import cn.shihh.zerojob.core.service.JobService;
import cn.shihh.zerojob.core.service.WheelService;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author shihh
 * @since 2024/10/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
@RocketMQMessageListener(
        consumerGroup = "zero-job-wheel-group",
        topic = "wheel-topic",
        selectorExpression = "job",
        consumeThreadNumber = 1, //默认是1个线程
        consumeThreadMax = 5 //默认是64个线程并发消息，配置 consumeThreadMax 参数指定并发消费线程数，避免太大导致资源不够
)
public class RocketMQWheelServiceImpl implements WheelService, RocketMQListener<String> {

    @Lazy
    @Resource
    private JobService jobService;
    private final RocketMQTemplate rocketMQTemplate;

    public static final String destination = "wheel-topic:job";

    @Override
    public void loadJobToWheel(Job job) {
        long jobExecuteDelayMs = job.getJobExecuteTime().getTime() - System.currentTimeMillis();
        if (jobExecuteDelayMs > 0) {
            log.info("[{}]将任务[{}]放入MQ时间轮，延迟[{}]ms", destination, job.getJobKey(), jobExecuteDelayMs);
        }
        Message<String> sendMessage = MessageBuilder.withPayload(job.getJobKey()).setHeader(RocketMQHeaders.KEYS, job.getJobKey()).build();
        SendResult sendResult = rocketMQTemplate.syncSendDelayTimeMills(destination, sendMessage, jobExecuteDelayMs);
        log.info("[{}]同步消息[{}]发送结果[{}]", destination, job.getJobKey(), JSONObject.toJSON(sendResult));
    }

    @Override
    public void removeJobFromWheel(String jobKey) {
        // 无法删除延迟队列中的消息
        log.info("[{}]从MQ时间轮中移除任务[{}]", destination, jobKey);
    }

    @Override
    public void onMessage(String message) {
        log.info("[{}]收到MQ时间轮消息[{}]", destination, message);
        jobService.executeJob(message);
    }
}
