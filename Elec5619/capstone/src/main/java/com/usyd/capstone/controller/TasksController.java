package com.usyd.capstone.controller;


import com.usyd.capstone.common.Result;
import com.usyd.capstone.entity.DTO.finalResponse;
import com.usyd.capstone.entity.Tasks;
import com.usyd.capstone.entity.VO.postTask;
import com.usyd.capstone.entity.VO.requestDistribute;
import com.usyd.capstone.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */
@RestController
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @PostMapping("/getDistribute")
    public Result getDistribute(@RequestBody requestDistribute query){


        finalResponse result = tasksService.distribute(query.getCV(), query.getTags());


        return Result.suc(result);
    }

    // customer post requirement
    @PostMapping("/postTask")
    public Result postTask(@RequestBody postTask task){
        Tasks newTask = new Tasks();
        newTask.setTaskDescribe(task.getTaskDescribe());
        newTask.setTaskLabel(task.getTaskLabel().toString());
        newTask.setTaskUserId(task.getTaskUserID());
        newTask.setTaskImageUrl(task.getTaskImgURL());
        boolean result = tasksService.save(newTask);
        if (result){
            return  Result.suc();
        }else {
            return Result.fail();
        }
    }

}

