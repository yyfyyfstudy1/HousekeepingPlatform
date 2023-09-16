package com.usyd.capstone.controller;


import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.DTO.finalResponse;
import com.usyd.capstone.entity.VO.TakeTask;
import com.usyd.capstone.entity.VO.requestDistribute;
import com.usyd.capstone.service.TaskOngoingService;
import com.usyd.capstone.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
    public Result takeTask(@RequestBody TakeTask takeTask){

        return taskOngoingService.updatePhaseByTaskId(takeTask);

    }



}

