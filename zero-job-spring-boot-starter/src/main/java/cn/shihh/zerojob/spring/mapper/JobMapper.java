package cn.shihh.zerojob.spring.mapper;

import cn.shihh.zerojob.core.model.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shihh
 * @since 2024/9/26
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {
}
