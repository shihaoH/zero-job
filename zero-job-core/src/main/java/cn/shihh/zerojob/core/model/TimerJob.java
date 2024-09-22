package cn.shihh.zerojob.core.model;

import cn.shihh.zerojob.core.enums.TypeClassEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author shihh
 * @date 2024/9/22
 */
@Data
public class TimerJob {

    private String jobKey;

    private Date jobExecuteTime;

    private String jobExecuteParam;

    private TypeClassEnum jobExecuteParamType;

}
