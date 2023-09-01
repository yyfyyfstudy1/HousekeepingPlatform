package com.usyd.capstone.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.DTO.finalResponse;
import com.usyd.capstone.entity.Tasks;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.EmailAddress;
import com.usyd.capstone.entity.VO.UpdatePasswordParameter;
import com.usyd.capstone.entity.VO.UserLogin;

import java.util.List;

public interface UserService extends IService<User> {

    List<User> listAll();

    IPage pageC(Page<User> page);

    IPage pageCC(Page<User> page, Wrapper wrapper);

    Result verifyLogin(UserLogin userLogin);

    Result registration(String email, String password, String firstname, String lastname);

    Result registrationVerification(String email, long registrationTimestamp, String passwordToken);

    Result forgetPassword(EmailAddress emailAddress);

    Result forgetPasswordVerification(String email, long registrationTimestamp);

    Result pollingResult(String email);

    Result updatePassword(UpdatePasswordParameter updatePasswordParameter);

}