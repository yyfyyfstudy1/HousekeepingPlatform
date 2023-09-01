package com.usyd.capstone.controller;

import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.VO.EmailAddress;
import com.usyd.capstone.entity.VO.UpdatePasswordParameter;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.entity.VO.UserRegistration;
import com.usyd.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result userLogin(@RequestBody UserLogin userLogin){
        System.out.println(userLogin);
        return userService.verifyLogin(userLogin);

    }

    @PostMapping("/forgetPassword")
    public Result forgetPassword(@RequestBody EmailAddress emailAddress){

        return userService.forgetPassword(emailAddress);

    }

    @GetMapping("/forgetPasswordVerification")
    public Result forgetPasswordVerification(@RequestParam("email") String email, @RequestParam("resettingPasswordTimestamp")
    long resettingPasswordTimestamp){
        return userService.forgetPasswordVerification(email, resettingPasswordTimestamp);
    }

    @GetMapping("/pollingResult")
    public Result pollingResult(@RequestParam("email") String email){

        return userService.pollingResult(email);
    }


    // TODO 需要完善加个token
    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordParameter updatePasswordParameter){

        return userService.updatePassword(updatePasswordParameter);

    }

    @PostMapping("/registration")
    public Result register(@RequestBody UserRegistration userRegistration){
        return userService.registration(userRegistration.getEmail(), userRegistration.getPassword(), userRegistration.getFirstname(), userRegistration.getLastname());
    }

    @GetMapping("/registrationVerification")
    public Result registrationVerification(@RequestParam("email") String email, @RequestParam("registrationTimestamp")
    long registrationTimestamp, @RequestParam("passwordToken") String passwordToken){
        return userService.registrationVerification(email, registrationTimestamp, passwordToken);
    }
}
