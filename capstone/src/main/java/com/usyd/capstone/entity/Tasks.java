package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Yyf
 * @since 2023年08月14日
 */

@Accessors(chain = true)
@TableName("tasks")
@Data
public class Tasks implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @TableField("task_describe")
    private String taskDescribe;

    @TableField("task_image_url")
    private String taskImageUrl;

    @TableField("task_user_id")
    private String taskUserId;

    @TableField("task_signed_id")
    private String taskSignedId;

    @TableField("task_label")
    private String taskLabel;

    @TableField(value = "Similarity", exist = false)
    private double Similarity;
}
