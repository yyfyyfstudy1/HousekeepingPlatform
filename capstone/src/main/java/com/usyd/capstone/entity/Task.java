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
 * @author Yyf
 * @since 2023年08月14日
 */

@Accessors(chain = true)
@TableName("tasks")
@Data
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @TableField("task_describe")
    private String taskDescribe;

    @TableField("task_image_url")
    private String taskImageUrl;

    @TableField("task_label")
    private String taskLabel;

    @TableField("category")
    private Integer category;

    @TableField("task_title")
    private String taskTitle;

    @TableField("task_salary")
    private String taskSalary;

    @TableField("task_user_id")
    private Integer taskUserId;

    @TableField("task_begin_time")
    private Long taskBeginTime;

    @TableField("task_estimated_duration")
    private String taskEstimatedDuration;

    @TableField("task_location")
    private String taskLocation;

    @TableField("task_is_locked")
    private Integer taskIsLocked;


    @TableField("task_is_finished")
    private Integer taskIsFinished;

    @TableField(value = "Similarity", exist = false)
    private double Similarity;

    @TableField("task_phase_describe")
    private String taskPhaseDescribe;

    @TableField("name")
    private String name;
}
