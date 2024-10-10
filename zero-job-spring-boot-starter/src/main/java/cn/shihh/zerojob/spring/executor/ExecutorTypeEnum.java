package cn.shihh.zerojob.spring.executor;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shihh
 * @since 2024/10/9
 */
@AllArgsConstructor
@Getter
public enum ExecutorTypeEnum {
    HTTP(1, "httpExecutor"),
    ROCKETMQ(2, "rocketMqExecutor"),
    RPC(3, "rpcExecutor"),
    ;
    private final int code;
    private final String name;
}
