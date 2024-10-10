package cn.shihh.zerojob.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shihh
 * @since 2024/10/9
 */
@AllArgsConstructor
@Getter
public enum EventStatus {
    RUNNING(1, "运行中"),
    COMPLETED(2, "已完成"),
    CANCELED(3, "已取消"),
    ;
    private final int code;
    private final String name;
}
