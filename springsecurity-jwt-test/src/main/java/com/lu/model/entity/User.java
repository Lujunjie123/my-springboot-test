package com.lu.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

//user类继承了 Spring Security的 UserDetails接口，从而成为了一个符合 Security安全的用户
@Data
@TableName("tb_user")
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class User implements Serializable{

    private static final long serialVersionUID = -6321573217443769099L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String password;

    private Boolean isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

}
