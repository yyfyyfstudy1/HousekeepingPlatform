package com.usyd.capstone.service;

import com.usyd.capstone.entity.DTO.finalResponse;
import com.usyd.capstone.entity.Tasks;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */
public interface TasksService extends IService<Tasks> {

    finalResponse distribute(String cv, List<String> tags);
}
