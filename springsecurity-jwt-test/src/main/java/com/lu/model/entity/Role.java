package com.lu.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.omg.CORBA.IDLType;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tb_role")
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
public class Role implements Serializable{

    private static final long serialVersionUID = -5449237502683346665L;
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private Boolean isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
