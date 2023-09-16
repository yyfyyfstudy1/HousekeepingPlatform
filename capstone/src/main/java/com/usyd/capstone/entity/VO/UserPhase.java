package com.usyd.capstone.entity.VO;

import lombok.Data;

@Data
public class UserPhase {
   // employer or labor
    private String userRole;
    private Integer userId;
    private Integer taskId;
}
