package com.usyd.capstone.entity.VO;

import lombok.Data;

import java.util.List;

@Data
public class postTask {
    private String taskDescribe;
    private String taskImgURL;
    private Integer taskUserID;
    private List<String> taskLabel;
}
