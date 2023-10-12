package com.usyd.capstone.entity.VO;

import lombok.Data;

import java.util.List;

@Data
public class requestDistribute {
    public String CV;

    public List<String> tags;

    public Integer userId;

    // 如果是刷新任务就传递这个参数
    public List<Integer> taskIDList;
}
