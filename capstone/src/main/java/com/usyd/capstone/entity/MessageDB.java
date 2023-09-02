package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年09月02日
 */
@Data
@Accessors(chain = true)
@TableName("message")
public class MessageDB implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("post_message_content")
    private String postMessageContent;

    @TableField("from_user_email")
    private String fromUserEmail;

    @TableField("to_user_email")
    private String toUserEmail;

    @TableField("post_time")
    private Long postTime;


}
