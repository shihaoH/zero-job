package cn.shihh.zerojob.spring.mapper;

import cn.hutool.core.date.DateTime;
import cn.shihh.zerojob.core.model.JobEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Mapper
public interface EventMapper extends BaseMapper<JobEvent> {
    List<JobEvent> getNextDayEvent(@Param("tomorrow") DateTime tomorrow);
}
