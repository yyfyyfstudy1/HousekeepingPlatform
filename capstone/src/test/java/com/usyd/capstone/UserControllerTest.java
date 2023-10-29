package com.usyd.capstone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.VO.*;
import com.usyd.capstone.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// Static imports
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private Long testUserId = 34L;

    private  Integer userId = 35;
    private String userEmail = "yiyu7699@uni.sydney.edu.au";
    @Test
    public void testUserLogin() throws Exception {
        // Given
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail("294006654@qq.com");
        userLogin.setPassword("1234321");

        // When & Then
        mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data.userInfo.id").value(testUserId));

    }
//    @Test
//    @Transactional
//    public void testForgetPassword() throws Exception {
//        // Given
//        EmailAddress emailAddress = new EmailAddress();
//        emailAddress.setEmailAddress("294006654@qq.com");
//
//        // When & Then
//        mockMvc.perform(post("/user/forgetPassword")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(emailAddress)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("successful"))
//                .andExpect(jsonPath("$.total").value(0))
//                .andExpect(jsonPath("$.data").value("The verification link has been sent to your email box"));
//    }

    @Test
    @Transactional
    public void testUpdatePassword() throws Exception {
        // Given
        UpdatePasswordParameter updatePasswordParameter = new UpdatePasswordParameter();
        updatePasswordParameter.setEmail("294006654@qq.com");
        updatePasswordParameter.setPassword("1234321");
        updatePasswordParameter.setPassword2("1234321");

        // When & Then
        mockMvc.perform(post("/user/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePasswordParameter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("fail"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data").value("your resetting password request hasn't been verity by email"));

        updatePasswordParameter.setEmail("chic924@icloud.com@mail.com");
        updatePasswordParameter.setPassword("1234322");
        updatePasswordParameter.setPassword2("1234321");

        mockMvc.perform(post("/user/updatePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatePasswordParameter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("fail"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data").value("error, can`t find this user"));
    }

    @Test
    @Transactional
    public void testRegistration() throws Exception {
        // Given
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setEmail("17789320@qq.com"+System.currentTimeMillis());
        userRegistration.setPassword("1234321");
        userRegistration.setFirstname("M");
        userRegistration.setLastname("L");

        // When & Then
        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("Registration successful! The verification link will be sent to your E-mail box."))
                .andExpect(jsonPath("$.total").value(0));
        userRegistration.setEmail("294006654@qq.com");
        mockMvc.perform(post("/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRegistration)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(409))
                .andExpect(jsonPath("$.msg").value("This email has been registered!"))
                .andExpect(jsonPath("$.total").value(0));
    }

//    @Test
//    @Transactional
//    public void testForgetPasswordVerification() throws Exception {
//        mockMvc.perform(get("/user/forgetPasswordVerification").param("email", "hiishikawa420@mail.com")
//                        .param("resettingPasswordTimestamp", "1621384321000"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(400))
//                .andExpect(jsonPath("$.msg").value("fail"))
//                .andExpect(jsonPath("$.total").value(0))
//                .andExpect(jsonPath("$.data").value("invalid verification link"));
//
//    }
    @Test
    @Transactional
    public void testRegistrationVerification() throws Exception {
        mockMvc.perform(get("/user/registrationVerification").param("email", "hiishikawa420@mail.com")
                        .param("registrationTimestamp", "1621384321000")
                        .param("passwordToken", "1234321"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.msg").value("The registration verification link is wrong!"))
                .andExpect(jsonPath("$.total").value(0));

    }
//    @Test
//    @Transactional
//    public void testPollingResult() throws Exception {
//        mockMvc.perform(get("/user/pollingResult").param("email", userEmail))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("successful"))
//                .andExpect(jsonPath("$.total").value(0))
//                .andExpect(jsonPath("$.data").value("Email verity successful"));
//        mockMvc.perform(get("/user/pollingResult").param("email", "hiishikawa420@mail.comss"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(400))
//                .andExpect(jsonPath("$.msg").value("fail"))
//                .andExpect(jsonPath("$.total").value(0))
//                .andExpect(jsonPath("$.data").value("error, the user doesn't exit"));
//    }

    @Test
    @Transactional
    public void testShowProfile() throws Exception {
        mockMvc.perform(get("/user/userInfo").param("email", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0))
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.name").value("yifan yu"));

    }



    // 将对象转换为 JSON 字符串的辅助方法
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
