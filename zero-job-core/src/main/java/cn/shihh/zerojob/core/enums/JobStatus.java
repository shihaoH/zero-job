package cn.shihh.zerojob.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shihh
 * @since 2024/9/23
 */
@RequiredArgsConstructor
@Getter
public enum JobStatus {
    CANCELLED(0, "已取消"),
    RUNNING(1, "运行中"),
    SUCCEED(2, "成功"),
    FAILED(3, "失败"),
    ;
    private final int code;
    private final String name;
}
