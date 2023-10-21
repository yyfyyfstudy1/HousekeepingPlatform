package com.usyd.capstone.entity.DTO;

import lombok.Data;

@Data
public class Notification {
    private Integer notificationId;
    private Integer taskId;
    private String status;
    private Integer phase;
}
