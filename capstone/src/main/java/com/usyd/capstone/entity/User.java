package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String no;
    private String name;
    private String password;
    private int sex;

    private String phone;
    @TableField("registration_timestamp")
    private long registrationTimestamp;

    @TableField("resetting_password_timestamp")
    private long resettingPasswordTimestamp;

    private String email;

    @TableField("activation_status")
    private boolean activationStatus;

    @TableField("forget_password_verity")
    private boolean forgetPasswordVerity;

    @TableField("address")
    private boolean address;

    @TableField("introduction")
    private boolean introduction;

}
