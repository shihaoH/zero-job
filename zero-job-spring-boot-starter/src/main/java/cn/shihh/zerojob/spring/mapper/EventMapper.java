package cn.shihh.zerojob.spring.mapper;

import cn.hutool.core.date.DateTime;
import cn.shihh.zerojob.core.model.JobEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author shihh
 * @since 2024/9/26
 */
public interface EventMapper extends BaseMapper<JobEvent> {
    List<JobEvent> getNextDayEvent(@Param("tomorrow") DateTime tomorrow);

    @Update("UPDATE job_event SET executed_count = executed_count + 1 WHERE event_id = #{jobGroup}")
    Integer addJobExecuteCount(@Param("jobGroup") String jobGroup);

    @Update("UPDATE job_event SET event_status = 'COMPLETED' WHERE event_id = #{jobGroup}")
    Integer completeJobEvent(@Param("jobGroup") String jobGroup);
}
