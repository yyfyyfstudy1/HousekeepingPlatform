package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年09月15日
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("task_ongoing")
public class TaskOngoing implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("task_id")
    private Integer taskId;

    @TableField("labor_id")
    private Integer laborId;

    @TableField("employer_id")
    private Integer employerId;

    @TableField("task_phase")
    private Integer taskPhase;

    @TableField("task_begin_time")
    private Long taskBeginTime;

    @TableField("task_phase_update_time")
    private Long taskPhaseUpdateTime;

    @TableField("labor_work_time")
    private Long laborWorkTime;

}
