package com.lu.constant;

public class Constant {

    public static final long EXPIRATION_TIME = 180_000;    //3分钟  //432_000_000  5天   token过期时间

    public static final String HEAD_STRING = "Authorization"; //存放token的header key

    public static final String TOKEN_PREFIX = "Bearer";   //token前缀

    public static final String SECRET = "LuSecret";  //Jwt密码
}
