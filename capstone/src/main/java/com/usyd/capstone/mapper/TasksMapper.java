package com.usyd.capstone.mapper;

import com.usyd.capstone.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */
@Mapper
public interface TasksMapper extends BaseMapper<Task> {

    List<Task> getPostedTaskByUserId(Integer userId);
    List<Task> getTakenTaskByUserId(Integer userId);

    List<Task> getEmployerTimeTableByUserID(Integer userId);

    List<Task> getLaborTimeTableByUserID(Integer userId);

    BigDecimal findSalaryById(Integer taskid);
}
