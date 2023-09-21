package com.usyd.capstone.mapper;

import com.usyd.capstone.entity.TaskOngoing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usyd.capstone.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyf
 * @since 2023年09月15日
 */
@Mapper
public interface TaskOngoingMapper extends BaseMapper<TaskOngoing> {

    User getTaskerInfoByTaskerId(Integer taskId);
}
