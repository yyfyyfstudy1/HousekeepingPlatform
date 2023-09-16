package com.usyd.capstone.service;

import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.TaskOngoing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usyd.capstone.entity.VO.TakeTask;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyf
 * @since 2023年09月15日
 */
public interface TaskOngoingService extends IService<TaskOngoing> {

    Result updatePhaseByTaskId(TakeTask takeTask);
}
