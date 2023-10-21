package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.component.NotificationServer;
import com.usyd.capstone.common.component.WebSocketServer;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.DTO.Notification;
import com.usyd.capstone.entity.TaskOngoing;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.TakeTask;
import com.usyd.capstone.entity.VO.UserPhase;
import com.usyd.capstone.mapper.TaskOngoingMapper;
import com.usyd.capstone.service.NotificationService;
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

    @Autowired
    NotificationService notificationService;

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
        taskOngoingOld.setLaborId(userPhase.getUserId());

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        taskOngoingOld.setTaskPhaseUpdateTime(timestamp);

       int i = taskOngoingMapper.updateById(taskOngoingOld);
       if (i!=0){
           return sendNotification(taskOngoingOld.getTaskId(), 2, "ok", taskOngoingOld.getEmployerId(), "Tasker have take the task");
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
            return sendNotification(taskOngoingOld.getTaskId(), 3, "ok", taskOngoingOld.getLaborId(), "employer has confirm the task");
        }
        return Result.fail();
        
    }

    @Override
    public Result getTaskerInfoByTaskId(Integer taskId) {
        User user = taskOngoingMapper.getTaskerInfoByTaskerId(taskId);
        if (user == null){
            return Result.fail();
        }
        return Result.suc(user);
    }

    @Override
    public Result laborConfirmArrived(UserPhase userPhase) {
        if (!userPhase.getUserRole().equals("labor")){
            return Result.fail();
        }
        TaskOngoing taskOngoingOld = taskOngoingMapper.selectOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", userPhase.getTaskId())
        );

        // employer confirm the task, phase 4
        taskOngoingOld.setTaskPhase(4);

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        taskOngoingOld.setTaskPhaseUpdateTime(timestamp);

        int i = taskOngoingMapper.updateById(taskOngoingOld);

        if (i!=0){
            return sendNotification(taskOngoingOld.getTaskId(), 4, "ok", taskOngoingOld.getEmployerId(), "Tasker has arrived");
        }
        return Result.fail();
    }


    @Override
    public Result laborStopTask(UserPhase userPhase) {
        if (!userPhase.getUserRole().equals("labor")){
            return Result.fail();
        }
        TaskOngoing taskOngoingOld = taskOngoingMapper.selectOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", userPhase.getTaskId())
        );

        // employer confirm the task, phase 14
        taskOngoingOld.setTaskPhase(14);

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        if (taskOngoingOld.getLaborWorkTime() == null){
            taskOngoingOld.setLaborWorkTime(0L);
        }
        // save work time
       taskOngoingOld.setLaborWorkTime(timestamp - taskOngoingOld.getTaskPhaseUpdateTime() + taskOngoingOld.getLaborWorkTime());

        int i = taskOngoingMapper.updateById(taskOngoingOld);

        if (i!=0){
            return sendNotification(taskOngoingOld.getTaskId(), 14, "no", taskOngoingOld.getEmployerId(), "Labor is paused the task");
        }
        return Result.fail();
    }

    @Override
    public Result laborRestartTask(UserPhase userPhase) {
        if (!userPhase.getUserRole().equals("labor")){
            return Result.fail();
        }
        TaskOngoing taskOngoingOld = taskOngoingMapper.selectOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", userPhase.getTaskId())
        );

        // employer confirm the task, phase 14
        taskOngoingOld.setTaskPhase(4);

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        // taskUpdateTime更新为restart的时间(当前时间)
        taskOngoingOld.setTaskPhaseUpdateTime(timestamp);

        int i = taskOngoingMapper.updateById(taskOngoingOld);

        if (i!=0){
            return sendNotification(taskOngoingOld.getTaskId(), 4, "no", taskOngoingOld.getEmployerId(), "Tasker is restart the task");
        }
        return Result.fail();
    }

    @Override
    public Result laborFinishedTask(UserPhase userPhase) {
        if (!userPhase.getUserRole().equals("labor")){
            return Result.fail();
        }
        TaskOngoing taskOngoingOld = taskOngoingMapper.selectOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", userPhase.getTaskId())
        );

        // employer confirm the task, phase 4
        taskOngoingOld.setTaskPhase(5);

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();

        // 保存工作时间
        if (taskOngoingOld.getLaborWorkTime() !=null){
            taskOngoingOld.setLaborWorkTime(timestamp - taskOngoingOld.getTaskPhaseUpdateTime() + taskOngoingOld.getLaborWorkTime());
        }else {
            taskOngoingOld.setLaborWorkTime(timestamp - taskOngoingOld.getTaskPhaseUpdateTime());
        }



        taskOngoingOld.setTaskPhaseUpdateTime(timestamp);

        int i = taskOngoingMapper.updateById(taskOngoingOld);

        if (i!=0){
            return sendNotification(taskOngoingOld.getTaskId(), 5, "ok", taskOngoingOld.getEmployerId(), "Tasker has finished task");
        }
        return Result.fail();
    }

    @Override
    public void employerPaymentSuccessful(Integer taskId) {
        TaskOngoing taskOngoingOld = taskOngoingMapper.selectOne(
                new QueryWrapper<TaskOngoing>().eq("task_id", taskId)
        );

        // employer payment succesful, phase 6
        taskOngoingOld.setTaskPhase(6);

        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        taskOngoingOld.setTaskPhaseUpdateTime(timestamp);

        int i = taskOngoingMapper.updateById(taskOngoingOld);

        if (i!=0){
            sendNotification(taskOngoingOld.getTaskId(), 6, "ok", taskOngoingOld.getLaborId(), "employer has finished payment");
        }

    }


    private  Result sendNotification(int taskId, int phase, String ok, Integer messageReceiverId, String notificationMessage) {



        // save notification to database
        com.usyd.capstone.entity.Notification notificationDB = new com.usyd.capstone.entity.Notification();
         notificationDB.setTaskId(taskId);
         notificationDB.setContent(notificationMessage);
         notificationDB.setIsRead(0);
         notificationDB.setReceivedUserId(messageReceiverId);
         notificationDB.setSendTime(System.currentTimeMillis());

         notificationService.save(notificationDB);



        Notification notification = new Notification();

        notification.setTaskId(taskId);
        notification.setPhase(phase);
        notification.setStatus(ok);

        // 设置notification的ID
        notification.setNotificationId(notificationDB.getId());
        String result = JSONObject.toJSONString(notification);



        // send message
        NotificationServer.sendMessage(result, messageReceiverId);

        return Result.suc(notificationMessage);
    }

}
