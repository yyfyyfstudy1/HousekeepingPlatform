package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年08月15日
 */
@Data
@Accessors(chain = true)
@TableName("task_tag")
public class TaskTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "tag_ID", type = IdType.AUTO)
    private Integer tagId;

    @TableField("tag_Name")
    private String tagName;

    @TableField("tag_Creater")
    private String tagCreater;


}
