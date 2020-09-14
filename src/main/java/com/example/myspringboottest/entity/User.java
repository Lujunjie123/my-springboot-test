package com.example.myspringboottest.entity;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties({ "sex" }) //忽略一组属性，作用于类上
//@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)  //用于指定一个命名策略 userName转化为user-name
public class User implements Serializable {
    private static final long serialVersionUID = 6222176558369919436L;

    //标识分组
    public interface UserNameView {};
    public interface AllUserFieldView extends UserNameView {};

    @JsonView(UserNameView.class)
    private String userName;

    @JsonView(UserNameView.class)
    private int age;

    @JsonView(UserNameView.class)
    private String password;

    //作用在属性上，用来为JSON Key指定一个别名
    @JsonView(AllUserFieldView.class)
    @JsonProperty("bth")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    //作用在属性上，用来忽略此属性
//    @JsonIgnore
    private int sex;
}