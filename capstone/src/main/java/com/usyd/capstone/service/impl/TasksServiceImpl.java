package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.DTO.*;
import com.usyd.capstone.entity.Task;
import com.usyd.capstone.mapper.TasksMapper;
import com.usyd.capstone.service.TasksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.sonatype.inject.Nullable;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.usyd.capstone.common.util.JaccardSimilarityExample.calculateJaccardSimilarity;
import static com.usyd.capstone.common.util.JaccardSimilarityExample.removeSpecialCharactersAndSpaces;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */
@Service
public class TasksServiceImpl extends ServiceImpl<TasksMapper, Task> implements TasksService {
    @Resource
    private TasksMapper tasksMapper;

    private String apiKey = "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";

    private final String chatGptApiUrl = "https://api.openai.com/v1/chat/completions"; // ChatGPT API URL
    @Override
    public List<finalResponse> distribute(String cv, List<String> tags, Integer userId, @Nullable List<Integer> taskIDlist) throws ExecutionException, InterruptedException {

        // get the top 3 of tag Similarity
        List<Task> taskGet;
        if (taskIDlist!=null){
            taskGet = calculateSimilarityTopTwo(tags, userId, taskIDlist);
        }else {
            taskGet = calculateSimilarityTopTwo(tags, userId, null);
        }

        if (taskGet == null){
            // 没算出两个任务就返回空
            return null;
        }

        int numberOfTasks = taskGet.size();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfTasks);
        List<Future<finalResponse>> futures = new ArrayList<>();

        // 同时触发多个异步任务，并将Future对象添加到列表中
        for (int i = 0; i < numberOfTasks; i++) {
            int finalI = i;
            Future<finalResponse> future = executorService.submit(() -> performAsyncTask(cv, taskGet.get(finalI)).get());
            futures.add(future);
        }

        // 等待所有任务执行完毕并收集结果
        List<finalResponse> results = new ArrayList<>();

        for (Future<finalResponse> future : futures) {
            finalResponse result = future.get();
            results.add(result);
        }

        // 关闭线程池
        executorService.shutdown();

        return results;
    }

    @Override
    public List<Task> getPostedTaskByUserId(Integer userId) {
        return tasksMapper.getPostedTaskByUserId(userId);
    }

    @Override
    public List<Task> getTakenTaskByUserId(Integer userId) {
        return tasksMapper.getTakenTaskByUserId(userId);
    }

    // 查询并且计算task，推送task
    private List<Task> calculateSimilarityTopTwo(List<String> tags, Integer userId, @Nullable List<Integer> taskIDlist){

        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("task_user_id", userId);

        if (taskIDlist!=null){
            for (Integer taskId : taskIDlist){
                queryWrapper.ne("task_id", taskId);
            }
        }

        List<Task> taskGet = tasksMapper.selectList(queryWrapper);

        if (taskGet.isEmpty()){
            return null;
        }
        List<Task> result = new ArrayList<>();

        for (Task task : taskGet) {

            List<String>  tagList = JSON.parseArray(task.getTaskLabel(), String.class);
            double jaccardSimilarity = calculateJaccardSimilarity(tagList, tags);

            if (jaccardSimilarity > 0) {  // Exclude results with similarity of 0
                task.setSimilarity(jaccardSimilarity);
                result.add(task);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(task.getTaskDescribe() + task.getSimilarity());
            }
        }

        if (result.isEmpty()){
            return null;
        }
        // sort by similarity
        result.sort(Comparator.comparingDouble(Task::getSimilarity).reversed());

        // get the top three
        return result.subList(0, Math.min(2, result.size()));
    }



    @Async
    public Future<finalResponse> performAsyncTask(String cv, Task task) {
        // 异步任务的逻辑，返回一个finalResponse结果
        finalResponse finalResponse = new finalResponse();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        StringBuilder use = new StringBuilder();
        use.append("Now,please play as a labor service intelligent platform, and there is a worker whose resume is <");
        use.append(cv);
        use.append("> there have a task that matches his label. Please analyze the reason why this task is suitable for him. Please don’t say any extra words, like the describe of task,  just give  your reason. When replying,Use the second person perspective, replace the word <my> and <his> with <your>." );

        use.append(" task:").append(removeSpecialCharactersAndSpaces(task.getTaskDescribe()));
        use.append(";");
        System.out.println(use);

        String requestBody = "{\"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"system\", \"content\": \"[clear context]\"}, {\"role\": \"user\", \"content\": \"" + use + "\"}], \"model\": \"gpt-3.5-turbo\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                chatGptApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
            ChatGptApiResponse chatCompletion = JSON.parseObject(response.getBody(), ChatGptApiResponse.class);
            Choice choice = chatCompletion.getChoices().get(0);
            Message message = choice.getMessage();
            String content = message.getContent();

            // set gpt response to finalResponse
            finalResponse.setTask(task);
            finalResponse.setGptReply(content);
            System.out.println(content);

        } else {
            // 处理错误情况
            Task errorTask = new Task();
            errorTask.setTaskId(999999);
            errorTask.setTaskDescribe("An error occurred while communicating with ChatGPT API.");
            finalResponse.setTask(errorTask);
            finalResponse.setGptReply("error error");
        }

        return new AsyncResult<>(finalResponse);
    }

    @Override
    public Result getTimeTableByUserID(Integer userId, Integer userType) {
        // 1 employer, 2 labor
        if (userType == 1){
            if (tasksMapper.getEmployerTimeTableByUserID(userId) !=null){
                return Result.suc(tasksMapper.getEmployerTimeTableByUserID(userId));
            }
        }else {
            if (tasksMapper.getLaborTimeTableByUserID(userId) !=null){
                return Result.suc(tasksMapper.getLaborTimeTableByUserID(userId));
            }
        }

        return Result.fail("service error");

    }
}
