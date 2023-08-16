package com.usyd.capstone.service.impl;

import com.alibaba.fastjson.JSON;
import com.usyd.capstone.entity.DTO.*;
import com.usyd.capstone.entity.Tasks;
import com.usyd.capstone.mapper.TasksMapper;
import com.usyd.capstone.service.TasksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.usyd.capstone.common.JaccardSimilarityExample.calculateJaccardSimilarity;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */
@Service
public class TasksServiceImpl extends ServiceImpl<TasksMapper, Tasks> implements TasksService {
    @Resource
    private TasksMapper tasksMapper;

    private String apiKey = "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";

    private final String chatGptApiUrl = "https://api.openai.com/v1/chat/completions"; // ChatGPT API URL
    @Override
    public finalResponse distribute(String cv, List<String> tags) {

        // get the top 5 of tag Similarity
        List<Tasks> tasksGet = calculateSimilarityTopFIve(tags);

        HashMap<String, String> tasks =new HashMap<>();
        for (Tasks tt: tasksGet){
            tasks.put(tt.getTaskId().toString(), tt.getTaskDescribe());
        }

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        StringBuilder use = new StringBuilder();
        use.append("现在我经营一个劳务平台，现在有一个劳动者，他的简历是<");
        use.append(cv);
        use.append(">现在作为一个劳务平台，我这里有多个任务与他的标签匹配，请你作为一个智能平台选出一个和他匹配度最高的，并且给出我理由。请不要说额外的话,回复的时候把<我>这个字替换成<你>." +
                "回复严格按照Json的格式 {'systemChoice':'(选择前面任务的编号数字)','reason':'因为你xxxxx'}");


        for (String key : tasks.keySet()) {
            String value = tasks.get(key);
            use.append(" 任务：").append(key).append(" ").append(value);
            use.append(";");
        }

        System.out.println(use);

        String requestBody = "{\"messages\": [{\"role\": \"system\", \"content\": \"You are a helpful assistant.\"}, {\"role\": \"system\", \"content\": \"[清除上下文]\"}, {\"role\": \"user\", \"content\": \"" + use + "\"}], \"model\": \"gpt-3.5-turbo\"}";



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
            // Use regex to replace single quotes with double quotes
            String result = content.replaceAll("'", "\"");

            // string to object
            GPTResponseJSON gptResponseJSON = JSON.parseObject(result, GPTResponseJSON.class);

            // find the selection of GPT
            Tasks choiceTask = tasksMapper.selectById(Integer.parseInt(gptResponseJSON.getSystemChoice()));

            finalResponse finalResponse = new finalResponse();
            finalResponse.setTasks(choiceTask);
            finalResponse.setGptReply(gptResponseJSON.getReason());

            System.out.println(gptResponseJSON.getReason());
            System.out.println(choiceTask);
            return finalResponse;
        } else {
            // 处理错误情况
            Tasks errorTask = new Tasks();
            errorTask.setTaskId(999999);
            errorTask.setTaskDescribe("An error occurred while communicating with ChatGPT API.");
            finalResponse finalResponse = new finalResponse();
            finalResponse.setTasks(errorTask);
            finalResponse.setGptReply("error error");
            return finalResponse;
        }
    }


    private List<Tasks> calculateSimilarityTopFIve(List<String> tags){
        List<Tasks> tasksGet = tasksMapper.selectList(null);
        List<Tasks> result = new ArrayList<>();

        for (Tasks task : tasksGet) {

            List<String>  tagList = JSON.parseArray(task.getTaskLabel(), String.class);
            double jaccardSimilarity = calculateJaccardSimilarity(tagList, tags);

            if (jaccardSimilarity > 0) {  // Exclude results with similarity of 0
                task.setSimilarity(jaccardSimilarity);
                result.add(task);
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(task.getTaskDescribe() + task.getSimilarity());
            }
        }

        // sort by similarity
        result.sort(Comparator.comparingDouble(Tasks::getSimilarity).reversed());

        // get the top five
        return result.subList(0, Math.min(3, result.size()));
    }
}
