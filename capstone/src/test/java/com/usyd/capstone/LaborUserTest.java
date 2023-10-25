package com.usyd.capstone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.entity.VO.UserPhase;
import com.usyd.capstone.entity.VO.requestDistribute;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LaborUserTest {
    @Autowired
    private MockMvc mockMvc;

    private String token;

    private Integer testTaskId = 115;

    private Integer testLaborId = 34;

    private Integer testEmployerId = 35;

    private Integer testTaskId2 = 130;

    @Autowired
    RedisTemplate redisTemplate;


    @Before
    public void loginAndGetToken() throws Exception {;
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
    @Transactional
    public void testTaskTakeTask() throws Exception {

        UserPhase userPhase = new UserPhase();
        userPhase.setUserRole("labor");
        userPhase.setTaskId(testTaskId);
        userPhase.setUserId(testLaborId);


        // When & Then
        mockMvc.perform(post("/public/tasks/takeTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(userPhase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").value("Tasker have take the task"));

    }


    @Test
    @Transactional
    public void testLaborConfirmArrived() throws Exception {

        UserPhase userPhase = new UserPhase();
        userPhase.setUserRole("labor");
        userPhase.setTaskId(testTaskId);
        userPhase.setUserId(testLaborId);


        // When & Then
        mockMvc.perform(post("/public/tasks/laborConfirmArrived")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(userPhase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").value("Tasker has arrived"));

    }



    @Test
    @Transactional
    public void testLaborStopTask() throws Exception {

        UserPhase userPhase = new UserPhase();
        userPhase.setUserRole("labor");
        userPhase.setTaskId(testTaskId);
        userPhase.setUserId(testLaborId);


        // When & Then
        mockMvc.perform(post("/public/tasks/laborStopTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(userPhase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").value("Labor is paused the task"));

    }


    @Test
    @Transactional
    public void testLaborRestartTask() throws Exception {

        UserPhase userPhase = new UserPhase();
        userPhase.setUserRole("labor");
        userPhase.setTaskId(testTaskId);
        userPhase.setUserId(testLaborId);


        // When & Then
        mockMvc.perform(post("/public/tasks/laborRestartTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(userPhase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").value("Tasker is restart the task"));

    }


    @Test
    @Transactional
    public void testLaborFinishedTask() throws Exception {

        UserPhase userPhase = new UserPhase();
        userPhase.setUserRole("labor");
        userPhase.setTaskId(testTaskId);
        userPhase.setUserId(testLaborId);


        // When & Then
        mockMvc.perform(post("/public/tasks/laborFinishedTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(userPhase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").value("Tasker has finished task"));

    }


    @Test
    public void testGetTimeTableByUserID() throws Exception {


        // When & Then
        mockMvc.perform(get("/public/tasks/getTimeTableByUserID")
                        .param("userId", String.valueOf(testEmployerId))
                        .param("userType", String.valueOf(1))

                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"));


    }


    @Test
    public void testGetLaborTimeTableByUserID() throws Exception {


        // When & Then
        mockMvc.perform(get("/public/tasks/getTimeTableByUserID")
                        .param("userId", String.valueOf(testLaborId))
                        .param("userType", String.valueOf(2))

                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"));


    }


    @Test
    public void testGetDistribute() throws Exception {


        requestDistribute requestDistribute = new requestDistribute();
        requestDistribute.setCV("good with everything");
        requestDistribute.setUserId(34);

        List<String> tags = new ArrayList<>();
        tags.add("1");
        requestDistribute.setTags(tags);

        // When & Then
        mockMvc.perform(post("/public/tasks/getDistribute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(requestDistribute)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data[0].task.taskId").value(testTaskId2));

    }



    @Test
    public void testGetDistributeRefresh() throws Exception {


        requestDistribute requestDistribute = new requestDistribute();
        requestDistribute.setCV("good with everything");
        requestDistribute.setUserId(34);

        List<Integer> taskIdList = new ArrayList<>();
        taskIdList.add(testTaskId2);
        requestDistribute.setTaskIDList(taskIdList);

        List<String> tags = new ArrayList<>();
        tags.add("1");
        requestDistribute.setTags(tags);

        // When & Then
        mockMvc.perform(post("/public/tasks/getDistribute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(requestDistribute)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.msg").value("fail"))
                .andExpect(jsonPath("$.data").value("No matching work"));

    }

}
