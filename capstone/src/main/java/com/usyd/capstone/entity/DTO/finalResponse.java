package com.usyd.capstone.entity.DTO;

import com.usyd.capstone.entity.Task;
import lombok.Data;

@Data
public class finalResponse {
    private Task task;
    private String GptReply;
}
