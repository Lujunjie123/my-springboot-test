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

    private String operation; //日志语句

    private String className;  //类名

    private String methodName;  //方法名

    private String params;      //方法参数

    private Long time;          //执行时间

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}



























