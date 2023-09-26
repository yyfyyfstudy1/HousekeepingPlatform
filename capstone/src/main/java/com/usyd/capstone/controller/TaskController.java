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
    public List<Task> showTasks(@RequestParam("taskUserId")  Integer userId){
        return tasksService.getTaskByUserId(userId);
    }
    @GetMapping("/List")
    public List<Task> list(){
        return tasksService.list();
    }
}

