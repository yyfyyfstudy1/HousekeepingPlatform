package com.usyd.capstone.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usyd.capstone.entity.DTO.finalResponse;
import com.usyd.capstone.entity.Tasks;
import com.usyd.capstone.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> listAll();

    IPage pageC(Page<User> page);

    IPage pageCC(Page<User> page, Wrapper wrapper);

}
