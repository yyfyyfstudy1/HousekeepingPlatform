package com.usyd.capstone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.usyd.capstone.entity.TaskTag;
import com.usyd.capstone.entity.VO.ModifyTaskInfoVO;
import com.usyd.capstone.entity.VO.TaskVO;
import com.usyd.capstone.entity.VO.UserLogin;
import com.usyd.capstone.entity.VO.UserPhase;
import com.usyd.capstone.service.TasksService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 确保数据库有taskId为114的数据，删除redis的key
 */


@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployerUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String token;


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
    @Transactional
    public void testUploadImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(
                multipart("/member/employer/uploadImage").file(file)
                        .header("Authorization", "Bearer " + this.token)
                )

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200)); // or any other assertions based on your Result structure
    }


    @Test
    @Transactional
    public void testEmployerConfirm() throws Exception {

        UserPhase userPhase = new UserPhase();
        userPhase.setUserRole("employer");
        userPhase.setUserId(0);
        userPhase.setTaskId(114);


        // When & Then
        mockMvc.perform(post("/member/employer/employerConfirmTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(userPhase)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0));

    }

    @Test
    @Transactional
    public void testGetCurrentTaskPhase() throws Exception {

        // 模拟GET请求
        mockMvc.perform(get("/member/employer/getCurrentTaskPhase")
                        .param("taskId", String.valueOf(114))
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0));
    }



    // 创建userService的模拟对象
//    @MockBean
//    private TasksService taskServiceMock;
//
//    @Test
//    public void getTaskDetailById() throws Exception {
//
//        // 配置模拟对象的行为
//        Task expectedTask = new Task();
//        expectedTask.setTaskDescribe("xxxxxxx");
//        when(taskServiceMock.getById(1)).thenReturn(expectedTask);
//
//
//        // 模拟GET请求
//        mockMvc.perform(get("/member/employer/getTaskDetailById")
//                        .param("taskId", String.valueOf(1)) // 使用1作为taskId，以匹配模拟对象的配置
//                        .header("Authorization", "Bearer " + this.token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("successful"))
//                .andExpect(jsonPath("$.total").value(0))
//                .andExpect(jsonPath("$.data").isNotEmpty()) // 验证"data"字段不为空
//                .andExpect(jsonPath("$.data.taskDescribe").value(expectedTask.getTaskDescribe()));
//
//
////        verify(taskServiceMock).getById(1);
////        verifyNoMoreInteractions(taskServiceMock);
//
//    }



    @Test
    @Transactional
    public void getTaskDetailById() throws Exception {

        // 模拟GET请求
        mockMvc.perform(get("/member/employer/getTaskDetailById")
                        .param("taskId", String.valueOf(114)) // 使用1作为taskId，以匹配模拟对象的配置
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").isNotEmpty()) // 验证"data"字段不为空
                .andExpect(jsonPath("$.data.taskDescribe").value("test modify"));

    }



    @Test
    @Transactional
    public void getTaskPhaseFourBeginTime() throws Exception {

        // 模拟GET请求
        mockMvc.perform(get("/member/employer/getTaskPhaseFourBeginTime")
                        .param("taskId", String.valueOf(114)) // 使用1作为taskId，以匹配模拟对象的配置
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").isNotEmpty()) // 验证"data"字段不为空
                .andExpect(jsonPath("$.data.taskBeginTime").value(11111111L));

    }


    @Test
    @Transactional
    public void getLaborWorkDuration() throws Exception {

        // 模拟GET请求
        mockMvc.perform(get("/member/employer/getLaborWorkDuration")
                        .param("taskId", String.valueOf(114)) // 使用1作为taskId，以匹配模拟对象的配置
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.data").isNotEmpty()) // 验证"data"字段不为空
                .andExpect(jsonPath("$.data").value(519891L));

    }


    @Test
    @Transactional
    public void editMyTask() throws Exception {
        ModifyTaskInfoVO modifyTaskInfoVO = new ModifyTaskInfoVO();

        modifyTaskInfoVO.setTaskId(114);
        modifyTaskInfoVO.setTaskDescribe("test modify");
        modifyTaskInfoVO.setTaskLocation("test location");
        modifyTaskInfoVO.setTaskBeginTime(11111111L);
        modifyTaskInfoVO.setTaskTitle("test Titlle");

        // When & Then
        mockMvc.perform(post("/member/employer/editMyTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(modifyTaskInfoVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"));

    }




    @Test
    @Transactional
    public void testPostTask() throws Exception {

        TaskVO taskVO = new TaskVO();
        taskVO.setCategory(1);
        taskVO.setDescribe("test task");
        taskVO.setDuration("3");
        taskVO.setJobCategory(1);
        taskVO.setLocation("Test location");
        taskVO.setSalary("300");

        List<TaskTag> tagList = new ArrayList();
        TaskTag taskTag = new TaskTag();
        taskTag.setTagCreater("user");
        taskTag.setTagName("aaaaa");
        taskTag.setCategory("xxxxx");
        taskTag.setTagId(12);
        tagList.add(taskTag);

        taskVO.setTags(tagList);

        taskVO.setTaskDate(String.valueOf(System.currentTimeMillis()));
        taskVO.setTaskTime("200");
        taskVO.setTaskTimeStamp(20000L);
        taskVO.setTitle("test task");
        taskVO.setUserID(2);
        taskVO.setImageUrl("testUrl");


        // When & Then
        MvcResult result = mockMvc.perform(post("/member/employer/postTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + this.token) // Adding token as Bearer type in Authorization header
                        .content(JSON.toJSONString(taskVO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"))
                .andExpect(jsonPath("$.total").value(0))
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JSONObject jsonResponse = JSON.parseObject(responseString);

        if (jsonResponse.containsKey("data")) {
            Integer newTaskId = jsonResponse.getInteger("data");


            // 模拟GET请求
            mockMvc.perform(get("/member/employer/deleteMyTask")
                            .param("taskId", String.valueOf(newTaskId))
                            .param("userRole", String.valueOf(1))
                            .header("Authorization", "Bearer " + this.token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(200))
                    .andExpect(jsonPath("$.msg").value("successful"));

        }

    }


    @Test
    @Transactional
    public void testDeleteMyTaskByTasker() throws Exception {

        // 模拟GET请求
        mockMvc.perform(get("/member/employer/deleteMyTask")
                        .param("taskId", String.valueOf(114))
                        .param("userRole", String.valueOf(2))
                        .header("Authorization", "Bearer " + this.token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("successful"));

    }


}
