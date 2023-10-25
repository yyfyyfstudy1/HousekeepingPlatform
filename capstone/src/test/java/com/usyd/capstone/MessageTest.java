package com.usyd.capstone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.usyd.capstone.entity.VO.GetMatchUserInfo;
import com.usyd.capstone.entity.VO.GetMessageHistory;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.entity.VO.UserPhase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    private String email1 = "yiyu7699@uni.sydney.edu.au";

    private Integer user1Id = 34;
    private Integer user2Id = 35;

    private String email2 = "294006654@qq.com";

    private Long firstMessageTime = 1697010943318L;

    @Before
    public void loginAndGetToken() throws Exception {
        // Given
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail("294006654@qq.com");
        userLogin.setPassword("1234321");

        // When & Then
        MvcResult mvcResult = mockMvc.perform(post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(userLogin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0))
                .andReturn();

        // Parse response to extract token
        String responseBody = mvcResult.getResponse().getContentAsString();
        JSONObject jsonResponse = JSON.parseObject(responseBody);
        this.token = jsonResponse.getJSONObject("data")
                .getJSONObject("authorization")
                .getString("token");
    }


    @Test
    public void testGetMessageHistory() throws Exception {

        GetMessageHistory getMessageHistory = new GetMessageHistory();
        getMessageHistory.setEmail1(email1);
        getMessageHistory.setEmail2(email2);

        // When & Then
        mockMvc.perform(post("/member/chatRoom/getMessageHistory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(getMessageHistory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data[0].postTime").value(firstMessageTime));

    }


    @Test
    public void testGetMatchUserInfo() throws Exception {

        GetMatchUserInfo getMatchUserInfo = new GetMatchUserInfo();
        getMatchUserInfo.setRole("employer");
        getMatchUserInfo.setUserId(35);

        // When & Then
        mockMvc.perform(post("/member/chatRoom/getMatchUserInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(getMatchUserInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data[0].name").value("test user"))
                .andExpect(jsonPath("$.data[0].sex").value(0))
                .andExpect(jsonPath("$.data[0].address").value("71 Aldwych, London WC2B 4HN, UK"))
                .andExpect(jsonPath("$.data[0].introduction").value("aaaa"))
                .andExpect(jsonPath("$.data[0].age").value(33))
                .andExpect(jsonPath("$.data[0].avatarUrl").value("http://localhost:8084/images1697891821969.jpg"));



    }


    @Test
    public void testGetMatchUserInfoByLabor() throws Exception {



        GetMatchUserInfo getMatchUserInfo1 = new GetMatchUserInfo();
        getMatchUserInfo1.setRole("labor");
        getMatchUserInfo1.setUserId(34);

        // When & Then
        mockMvc.perform(post("/member/chatRoom/getMatchUserInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(getMatchUserInfo1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data[0].name").value("yifan yu"));


    }


    @Test
    public void testGetUnreadNotification() throws Exception {

        // When & Then
        mockMvc.perform(get("/member/notification/getUnreadNotification")
                        .param("userId", String.valueOf(user1Id))
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data[0].id").value(167))
                .andExpect(jsonPath("$.data[0].content").value("tasker has cancel the task [ test Titlle ]"))
                .andExpect(jsonPath("$.data[0].sendTime").value(1698023103155L))
                .andExpect(jsonPath("$.data[0].taskId").value(114));


    }


    @Test
    public void testGetAllNotification() throws Exception {

        // When & Then
        mockMvc.perform(get("/member/notification/getAllNotification")
                        .param("userId", String.valueOf(user1Id))
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data[0].id").value(167));


    }


    @Test
    @Transactional
    public void testReadNotification() throws Exception {

        // When & Then
        mockMvc.perform(get("/member/notification/readNotification")
                        .param("notificationId", String.valueOf(173))
                        .param("isRead", String.valueOf(1))
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"));

    }



    @Test
    @Transactional
    public void testDeleteNotification() throws Exception {

        // When & Then
        mockMvc.perform(get("/member/notification/deleteNotification")
                        .param("notificationId", String.valueOf(173))
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"));

    }
}
