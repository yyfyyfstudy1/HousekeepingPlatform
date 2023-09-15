package com.usyd.capstone.entity.VO;

import com.alibaba.fastjson.JSON;
import com.usyd.capstone.entity.TaskTag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TaskVO {
    private int category;
    private String describe;
    private String duration;
    private int jobCategory;
    private String location;
    private String salary;
    private List<TaskTag> tags;
    private String taskDate;
    private String taskTime;
    private long taskTimeStamp;
    private String title;
    private int userID;
    private String imageUrl;

    public String getLabels() {
        List<Integer> labelList = new ArrayList<>();

        for (TaskTag tt : tags){
            labelList.add(tt.getTagId());
        }
        // 使用FastJson将List转换为JSON字符串
        String jsonString = JSON.toJSONString(labelList);

        return jsonString;
    }
}
