package com.usyd.capstone.service;

import com.usyd.capstone.entity.DTO.finalResponse;
import com.usyd.capstone.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */
public interface TasksService extends IService<Task> {

    List<finalResponse> distribute(String cv, List<String> tags) throws ExecutionException, InterruptedException;
}
