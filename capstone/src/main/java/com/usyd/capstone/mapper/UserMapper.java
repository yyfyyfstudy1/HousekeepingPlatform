package com.usyd.capstone.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.Role;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.ProfileUpdate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<User> listAll();

    IPage pageC(Page<User> page);

    IPage pageCC(Page<User> page, Wrapper ew);

    List<Role> findRoles(Long id);

    User findByEmail(String email);

    void updateAnOldInactivatedUser(User userOld);

    User findByEmailAndRegistrationTimestampAndPassword(String email, long registrationTimestamp, String password);

    void activeAnAccount(User user);


    void createUserRoleById(Long userId, Integer roleId);

    List<User> finelec5619User();

    Boolean updateProfile(ProfileUpdate profileUpdate);
}
