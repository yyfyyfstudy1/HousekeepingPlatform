package com.usyd.capstone;

import com.usyd.capstone.entity.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TasksServiceTest {

    @Autowired
    private com.usyd.capstone.service.TasksService tasksService;


    @Test
    public void getPostedTaskByUserId() {
        Integer userId = 25;
        // 使用默认构造函数创建Task对象
        Task expectedTask = new Task();

        // 使用setter方法设置字段值
        expectedTask.setTaskId(78);
        expectedTask.setTaskTitle("Professional Cleaning Services");
        expectedTask.setTaskSalary("50");
        expectedTask.setTaskBeginTime(0L);
        expectedTask.setTaskEstimatedDuration("2");
        expectedTask.setTaskPhaseDescribe("matching labor");
        expectedTask.setTaskLocation("");
        // ... 设置其他字段

        // 创建预期的List
        List<Task> expectedList = new ArrayList<>();
        expectedList.add(expectedTask);

        // 断言预期的List和函数返回的List是否相同
        assertEquals(expectedList, tasksService.getPostedTaskByUserId(userId));
    }

    @Test
    public void getTakenTaskByUserId() {
        Integer userId = 25;
        // 使用默认构造函数创建Task对象
        Task expectedTask = new Task();

        // 使用setter方法设置字段值
        expectedTask.setTaskId(77);
        expectedTask.setTaskTitle("test job ");
        expectedTask.setTaskSalary("111");
        expectedTask.setTaskBeginTime(1695287070000L);
        expectedTask.setTaskEstimatedDuration("4");
        expectedTask.setTaskPhaseDescribe("labor take order");
        expectedTask.setTaskLocation("regain street 66");
        expectedTask.setName("ronghui shao");
        // ... 设置其他字段

        // 创建预期的List
        List<Task> expectedList = new ArrayList<>();
        expectedList.add(expectedTask);

        // 断言预期的List和函数返回的List是否相同
        assertEquals(expectedList, tasksService.getTakenTaskByUserId(userId));
    }

}