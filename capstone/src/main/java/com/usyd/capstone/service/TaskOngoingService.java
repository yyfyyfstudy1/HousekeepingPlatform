package com.usyd.capstone.service;

import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.TaskOngoing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usyd.capstone.entity.VO.TakeTask;
import com.usyd.capstone.entity.VO.UserPhase;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyf
 * @since 2023年09月15日
 */
public interface TaskOngoingService extends IService<TaskOngoing> {

    Result laborTakeTask(UserPhase userPhase);

    Result employerConfirmTask(UserPhase userPhase);

    Result getTaskerInfoByTaskId(Integer taskId);

    Result laborConfirmArrived(UserPhase userPhase);

    Result laborStopTask(UserPhase userPhase);
}
