package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.TaskTag;
import com.usyd.capstone.mapper.TaskTagMapper;
import com.usyd.capstone.service.TaskTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyf
 * @since 2023年08月15日
 */
@Service
public class TaskTagServiceImpl extends ServiceImpl<TaskTagMapper, TaskTag> implements TaskTagService {
}
