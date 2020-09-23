package com.lu.Entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("log")
@AllArgsConstructor
@NoArgsConstructor
public class SysLog {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String username;

    private String operation;

    private String className;

    private String methodName;

    private String params;

    private Long time;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}



























