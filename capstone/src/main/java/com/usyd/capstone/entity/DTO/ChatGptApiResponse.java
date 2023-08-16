package com.usyd.capstone.entity.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ChatGptApiResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
}

@Data
class Usage {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
}
