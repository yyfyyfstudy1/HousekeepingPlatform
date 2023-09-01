package com.usyd.capstone.controller;


import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.service.TaskTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyf
 * @since 2023年08月15日
 */
@RestController
@RequestMapping("/public/taskTag")
public class TaskTagController {

    @Autowired
    private TaskTagService taskTagService;

    @GetMapping("/getTags")
    public Result getTags(){
        return Result.suc(taskTagService.list());
    }

}

