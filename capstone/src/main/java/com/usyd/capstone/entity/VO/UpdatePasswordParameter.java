package com.usyd.capstone.entity.VO;

import lombok.Data;

@Data
public class UpdatePasswordParameter {
    private String email;
    private String password;
    private String password2;
}
