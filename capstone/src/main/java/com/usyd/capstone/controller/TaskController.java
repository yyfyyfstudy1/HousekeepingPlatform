package com.usyd.capstone.controller;

import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.Task;
import com.usyd.capstone.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class TaskController {
    @Autowired
    private TasksService tasksService;

    @GetMapping("/myTask")
    public List<Task> showPostedTasks(@RequestParam("userId")  Integer userId){
        return tasksService.getPostedTaskByUserId(userId);
    }
    @GetMapping("/myTokenTask")
    public List<Task> showTokenTasks(@RequestParam("userId")  Integer userId){
        return tasksService.getTokenTaskByUserId(userId);
    }
}

