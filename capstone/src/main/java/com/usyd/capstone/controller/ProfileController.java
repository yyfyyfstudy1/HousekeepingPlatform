package com.usyd.capstone.controller;

import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.User;
import com.usyd.capstone.entity.VO.ProfileUpdate;
import com.usyd.capstone.entity.VO.UpdatePasswordParameter;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/user")
public class ProfileController {
    @Autowired
    private UserService userService;
    @GetMapping("/profile")
    public Result showProfile(@RequestParam("id")  String id){
        User user = userService.getById(id);
        return Result.suc(user);
    }
    @PostMapping("/update")
    public Result updateProfile(@RequestBody ProfileUpdate profileUpdate){
        return userService.updateProfile(profileUpdate);
    }
}
