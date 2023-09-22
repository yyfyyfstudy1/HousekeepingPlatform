package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.DTO.finalResponse;
import com.usyd.capstone.entity.TaskOngoing;
import com.usyd.capstone.entity.VO.TakeTask;
import com.usyd.capstone.entity.VO.UserPhase;
import com.usyd.capstone.entity.VO.requestDistribute;
import com.usyd.capstone.service.TaskOngoingService;
import com.usyd.capstone.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */
@RestController
@RequestMapping("/public/tasks")
public class LaborUserController {

    @Autowired
    private TasksService tasksService;

    @Autowired
    private TaskOngoingService taskOngoingService;

    @PostMapping("/getDistribute")
    public Result getDistribute(@RequestBody requestDistribute query) throws ExecutionException, InterruptedException {


        List<finalResponse> result = tasksService.distribute(query.getCV(), query.getTags());


        return Result.suc(result);
    }
    @PostMapping("/takeTask")
    public Result takeTask(@RequestBody UserPhase userPhase){

        return taskOngoingService.laborTakeTask(userPhase);

    }

    @GetMapping("/getTaskerInfo")
    public Result getTaskInfo(@RequestParam("taskId") Integer taskId){
        return taskOngoingService.getTaskerInfoByTaskId(taskId);
    }

    @PostMapping("/laborConfirmArrived")
    public Result employerConfirmTask(@RequestBody UserPhase userPhase){
        return   taskOngoingService.laborConfirmArrived(userPhase);

    }

    @PostMapping("/laborStopTask")
    public Result laborStopTask(@RequestBody UserPhase userPhase){
        return   taskOngoingService.laborStopTask(userPhase);

    }


}

