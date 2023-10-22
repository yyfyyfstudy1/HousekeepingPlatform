package com.usyd.capstone.entity.VO;

import lombok.Data;

@Data
public class ModifyTaskInfoVO {
    private Integer taskId;
    private Long taskBeginTime;
    private String taskLocation;
    private String taskDescribe;
    private String taskTitle;
}
