package com.usyd.capstone.controller;


import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.service.TaskCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyf
 * @since 2023年09月09日
 */
@RestController
@RequestMapping("/public/taskCategory")
public class TaskCategoryController {
    @Autowired
    TaskCategoryService taskCategoryService;
    @GetMapping("/allCategory")
    public Result getAllCategory(){
       return Result.suc(taskCategoryService.list());
    }

}

