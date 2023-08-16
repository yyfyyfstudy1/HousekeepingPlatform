package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.QueryPageParam;
import com.usyd.capstone.common.Result;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.service.TaskTagService;
import com.usyd.capstone.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyf
 * @since 2023年08月15日
 */
@RestController
@RequestMapping("/taskTag")
public class TaskTagController {

    @Autowired
    private TaskTagService taskTagService;

    @GetMapping("/getTags")
    public Result getTags(){
        return Result.suc(taskTagService.list());
    }

}

