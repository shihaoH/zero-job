package cn.shihh.zerojob.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Getter
@RequiredArgsConstructor
public enum JobTypeEnum {
    /**
     * 定时任务
     */
    SCHEDULED(1, "定时任务"),
    /**
     * 一次性任务
     */
    ONE_TIME(2, "一次性任务"),
    /**
     * 周期性任务
     */
    RECURRING(3, "周期性任务"),
    ;
    private final int code;
    private final String desc;
}
