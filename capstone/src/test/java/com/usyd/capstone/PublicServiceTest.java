package com.usyd.capstone;

import com.usyd.capstone.entity.VO.EmailAddress;
import com.usyd.capstone.service.UserService;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PublicServiceTest {

    @Autowired
    private UserService UserService;


    @Test
    public void forgetPassword() {
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setEmailAddress("testWrongEmail");
        assertEquals("your email address is wrong", UserService.forgetPassword(emailAddress).getData());

    }

    @Test
    public void forgetPasswordVerification() {
        assertEquals("invalid verification link", UserService.forgetPasswordVerification("aaa", 123).getData());
    }

}