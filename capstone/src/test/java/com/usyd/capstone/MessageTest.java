package com.usyd.capstone;

import com.alibaba.fastjson.JSON;
import com.usyd.capstone.entity.VO.UserPhase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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



    @Test
    public void testGetMessageHistory() throws Exception {

        UserPhase userPhase = new UserPhase();
        userPhase.setUserRole("labor");


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
}
