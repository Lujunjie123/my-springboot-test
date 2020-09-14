package com.example.myspringboottest.controller;

import com.example.myspringboottest.entity.User;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
public class JsonController {

    @Autowired
    ObjectMapper mapper;

    @JsonView(User.UserNameView.class)
    @GetMapping("getuser")
    public User getUser() {
        User user = new User();
        user.setUserName("mrbird");
        user.setBirthday(new Date());
        return user;
    }

    //序列化
    @GetMapping("serialization")
    public String serialization() {
        try {
            User user = new User();
            user.setUserName("mrbird");
            user.setBirthday(new Date());
            String str = mapper.writeValueAsString(user);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //反序列化
    @GetMapping("readjsonstring")
    public String readJsonString() {
        try {
//            String json = "{\"name\":\"mrbird\",\"age\":26}";
//            JsonNode node = this.mapper.readTree(json);
//            String name = node.get("name").asText();
//            int age = node.get("age").asInt();
//            return name + " " + age;          //mrbird 26

            String json = "{\"name\":\"mrbird\",\"hobby\":{\"first\":\"sleep\",\"second\":\"eat\"}}";;
            JsonNode node = this.mapper.readTree(json);
            JsonNode hobby = node.get("hobby");
            String first = hobby.get("first").asText();
            return first;       //sleep
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //绑定对象
    @GetMapping("readjsonasobject")
    public String readJsonAsObject() {
        try {
            String json = "{\"userName\":\"mrbird\",\"age\":26}";
            User user = mapper.readValue(json, User.class);
            String name = user.getUserName();
            int age = user.getAge();
            return name + " " + age;        //mrbird 26
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //集合反序列化写法
    @JsonView(User.AllUserFieldView.class)
    @GetMapping("customize")
    public String customize() throws JsonParseException, JsonMappingException, IOException {
        String jsonStr = "[{\"userName\":\"mrbird\",\"age\":26},{\"userName\":\"scott\",\"age\":27}]";
        JavaType type = mapper.getTypeFactory().constructParametricType(List.class, User.class);
        List<User> list = mapper.readValue(jsonStr, type);
        String msg = "";
        for (User user : list) {
            msg += user.getUserName();
        }
        return msg; //mrbirdscott
    }
}
