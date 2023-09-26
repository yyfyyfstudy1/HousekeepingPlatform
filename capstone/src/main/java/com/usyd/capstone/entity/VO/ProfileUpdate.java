package com.usyd.capstone.entity.VO;
import lombok.Data;

@Data
public class ProfileUpdate {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private String age;
    private String introduction;
    private String avatarUrl;
}
