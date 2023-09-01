package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usyd.capstone.common.Enums.PublicKey;
import com.usyd.capstone.common.Token;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.common.util.SendEmail;
import com.usyd.capstone.common.util.TokenUtils;
import com.usyd.capstone.entity.DTO.userUse;
import com.usyd.capstone.entity.Role;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.EmailAddress;
import com.usyd.capstone.entity.VO.UpdatePasswordParameter;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.mapper.RoleMapper;
import com.usyd.capstone.mapper.UserMapper;
import com.usyd.capstone.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SendEmail sentEmail;


    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }

    @Override
    public IPage pageC(Page<User> page) {
        return userMapper.pageC(page);
    }

    @Override
    public IPage pageCC(Page<User> page, Wrapper wrapper) {
        return userMapper.pageCC(page, wrapper);
    }

    @Override
    public Result verifyLogin(UserLogin userLogin) {
        if (StringUtils.isEmpty(userLogin.getPassword()) || StringUtils.isEmpty(userLogin.getEmail())){
            return Result.fail("Empty Email or password");
        }

        // Verify user
        User dbUser = userMapper.selectOne(new QueryWrapper<User>().eq("email", userLogin.getEmail()));
        if (dbUser == null) {
            return Result.fail("Wrong email or password");
        }

        String machUse = userLogin.getEmail() + userLogin.getPassword() + PublicKey.firstKey.getValue();

        if (!passwordEncoder.matches(machUse, dbUser.getPassword())){
            return Result.fail("Wrong email or password");
        }

        if (!dbUser.isActivationStatus()){
            return Result.fail("your account has not been activation");
        }

        // find roles of user
        List<Role> roles= userMapper.findRoles(dbUser.getId());

        List RolesResult = new ArrayList();

        for (Role role : roles){
            RolesResult.add(role.getRoleName());
        }

        String commaSeparated = (String) RolesResult.stream().collect(Collectors.joining(","));

        Map<String, String> param = new HashMap<>();
        param.put("userId", dbUser.getId().toString());
        param.put("roles", commaSeparated);
        Token token = TokenUtils.createJwt(param, 100000L);

        userUse userUse = new userUse();
        userUse.setId(dbUser.getId());
        userUse.setEmail(dbUser.getEmail());
        userUse.setName(dbUser.getName());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("authorization", token);
        resultMap.put("roles", RolesResult);
        resultMap.put("userInfo", userUse);

        return Result.suc(resultMap);
    }

    @Override
    //如果用户不存在->存一个新用户+发邮件+返回“已保存”
    //如果存在->如果已激活->报错“已激活”
    //如果存在->如果未激活->更新该用户+发邮件+返回“已更新”
    public Result registration(String email, String password, String firstname, String lastname){
        long registrationTimeStamp = System.currentTimeMillis();
        String passwordToken = passwordEncoder.encode(email + password + PublicKey.firstKey.getValue());
        User userOld = userMapper.findByEmail(email);
        if(userOld == null)
        {
            User userNew = new User();
            userNew.setEmail(email);
            userNew.setName(firstname +' '+ lastname);
            userNew.setRegistrationTimestamp(registrationTimeStamp);
            userNew.setPassword(passwordToken);
            userNew.setActivationStatus(false);
            sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken);
            // 可以直接调用mybatisplus的insert方法
            userMapper.insert(userNew);

            return Result.customize(200, "Registration successful! The verification link will be " +
                    "sent to your E-mail box.", 0L, null);
        }
        else
        {
            if(userOld.isActivationStatus())
            {
                return Result.customize(409, "This email has been registered!", 0L, null);
            }
            else {
                userOld.setRegistrationTimestamp(registrationTimeStamp);
                userOld.setPassword(passwordToken);
                sentEmail.sentRegistrationEmail(email, registrationTimeStamp, passwordToken);
                userMapper.updateAnOldInactivatedUser(userOld);
                return Result.customize(200, "Registration successful! The verification link will be " +
                        "sent to your E-mail box.", 0L, null);
            }
        }
    }



    @Override
    public Result registrationVerification(String email, long registrationTimestamp, String password) {
        User user = userMapper.findByEmailAndRegistrationTimestampAndPassword(email, registrationTimestamp, password);
        if(user == null)
        {
            return Result.customize(404, "The registration verification link is wrong!", 0L, null);
        }
        else
        {
            if(user.isActivationStatus())
            {
                return Result.customize(400, "This is an active account!", 0L, null);
            }
            else if(System.currentTimeMillis() - registrationTimestamp > 86400000L)
            {
                return Result.customize(410, "The registration verification link is out of date!", 0L, null);
            }
            else
            {

                // find roleID (create a general user)
                Role role =  roleMapper.selectOne(new QueryWrapper<Role>().eq("role_name", "ROLE_USER"));

                // insert with general user role
                userMapper.createUserRoleById(user.getId(), role.getId());

                userMapper.activeAnAccount(user);
                return Result.customize(200, "Your account has been activated!", 0L, null);
            }
        }
    }

    @Override
    public Result forgetPassword(EmailAddress emailAddress) {


        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", emailAddress.getEmailAddress()));
        if (user == null){
            return Result.fail("your email address is wrong");
        }
        user.setResettingPasswordTimestamp(System.currentTimeMillis());

        if (!user.isActivationStatus()){
            return Result.fail("your account is not active");
        }

        // send verify email
        sentEmail.sentForgetEmail( emailAddress.getEmailAddress(), user.getResettingPasswordTimestamp());
        userMapper.updateById(user);
        return Result.suc("The verification link has been sent to your email box");
    }

    @Override
    public Result forgetPasswordVerification(String email, long resettingPasswordTimestamp) {

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null){
            return Result.fail("invalid verification link");
        }
        if(user.getResettingPasswordTimestamp() != resettingPasswordTimestamp)
        {
            return Result.fail("invalid verification link");
        }
        if (System.currentTimeMillis() - resettingPasswordTimestamp > 86400000L){
            return Result.fail("The resetting password verification link is out of date!");
        }
        user.setResettingPasswordTimestamp(System.currentTimeMillis());
        user.setForgetPasswordVerity(true);
        userMapper.updateById(user);
        return Result.suc("The resetting password verification has been verified successfully!" +
                "You will have 30 minutes to set a new password");
    }

    @Override
    public Result pollingResult(String email) {

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));

        if (user == null){
            return Result.fail("error, the user doesn't exit");
        }

        if (user.isForgetPasswordVerity()){
            return Result.suc("Email verity successful");
        }else {
            return Result.fail("Email still not verity");
        }
    }

    //TODO 检测两个密码是否一致的功能移到前端（？）
    @Override
    public Result updatePassword(UpdatePasswordParameter updatePasswordParameter) {

        String email = updatePasswordParameter.getEmail();
        String password = updatePasswordParameter.getPassword();
        String passwordTwo = updatePasswordParameter.getPassword2();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (user == null){
            return Result.fail("error, can`t find this user");
        }
        if (!user.isForgetPasswordVerity()){
            return Result.fail("your resetting password request hasn't been verity by email");
        }
        if (System.currentTimeMillis() - user.getResettingPasswordTimestamp() > 1800000L)
        {
            user.setForgetPasswordVerity(false);
            userMapper.updateById(user);
            return Result.fail("resetting password permission has been out of date.");
        }
        if (!password.equals(passwordTwo)){
            return Result.fail("your two password is not same ");
        }
        // encode password
        String passwordToken = passwordEncoder.encode(email + password + PublicKey.firstKey.getValue());
        user.setPassword(passwordToken);
        user.setForgetPasswordVerity(false);

        userMapper.updateById(user);

        return Result.suc("user password has been update");
    }




}
