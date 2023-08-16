package com.usyd.capstone.entity.DTO;

import com.usyd.capstone.entity.Tasks;
import lombok.Data;

@Data
public class finalResponse {
    private Tasks tasks;
    private String GptReply;
}
