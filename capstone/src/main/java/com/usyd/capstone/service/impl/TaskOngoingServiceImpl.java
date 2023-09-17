package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.component.NotificationServer;
import com.usyd.capstone.common.component.WebSocketServer;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.DTO.Notification;
import com.usyd.capstone.entity.TaskOngoing;
import com.usyd.capstone.entity.VO.TakeTask;
import com.usyd.capstone.entity.VO.UserPhase;
import com.usyd.capstone.mapper.TaskOngoingMapper;
import com.usyd.capstone.service.TaskOngoingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyf
 * @since 2023年09月15日
 */
@Service
public class TaskOngoingServiceImpl extends ServiceImpl<TaskOngoingMapper, TaskOngoing> implements TaskOngoingService {
    @Autowired
    TaskOngoingMapper taskOngoingMapper;
    @Override
    public Result laborTakeTask(UserPhase userPhase) {

        if (!userPhase.getUserRole().equals("labor")){
            return Result.fail();
        }
        TaskOngoing taskOngoingOld = taskOngoingMapper.selectOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", userPhase.getTaskId())
        );

        // labor have take the order, phase 2
        taskOngoingOld.setTaskPhase(2);
        taskOngoingOld.setLaberId(userPhase.getUserId());

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        taskOngoingOld.setTaskPhaseUpdateTime(timestamp);

       int i = taskOngoingMapper.updateById(taskOngoingOld);
       if (i!=0){
           Notification notification = new Notification();
           // send message to employer {'taskId':'22', 'status':'ok'}
           notification.setTaskId(taskOngoingOld.getTaskId());
           notification.setPhase(2);
           notification.setStatus("ok");

            String result = JSONObject.toJSONString(notification);
           NotificationServer.sendMessage(result, taskOngoingOld.getEmployerId());

           return Result.suc("labor have take the task");
       }
       return Result.fail();
    }

    @Override
    public Result employerConfirmTask(UserPhase userPhase) {
        if (!userPhase.getUserRole().equals("employer")){
            return Result.fail();
        }
        TaskOngoing taskOngoingOld = taskOngoingMapper.selectOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", userPhase.getTaskId())
        );

        // employer confirm the task, phase 3
        taskOngoingOld.setTaskPhase(3);

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        taskOngoingOld.setTaskPhaseUpdateTime(timestamp);

        int i = taskOngoingMapper.updateById(taskOngoingOld);

        if (i!=0){
            Notification notification = new Notification();
            // send message to employer {'taskId':'22', 'status':'ok'}
            notification.setTaskId(taskOngoingOld.getTaskId());
            notification.setPhase(3);
            notification.setStatus("ok");

            String result = JSONObject.toJSONString(notification);

            // send message to labor
            NotificationServer.sendMessage(result, taskOngoingOld.getLaberId());

            return Result.suc("employer has confirm the task");
        }
        return Result.fail();
        
    }
}
