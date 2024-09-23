package cn.shihh.zerojob.core.enums;

/**
 * @author shihh
 * @since 2024/9/23
 */
public enum JobStatus {
    CANCELLED(0, "已取消"),
    RUNNING(1, "运行中"),
    SUCCEED(2, "成功"),
    FAILED(3, "失败"),
    ;
    public final int code;
    public final String name;

    JobStatus(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
