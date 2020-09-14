package com.lu.util;

import com.lu.constant.Constant;
import com.lu.model.entity.SysUser;
import com.sun.istack.internal.NotNull;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";

    //从token中获取主体信息
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try{
            claims =  Jwts.parser()
                    .setSigningKey(Constant.SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }

    //从token中获取username
    public String getUsernameFromToken(String token){
        String username;
        try{
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            username = null;
        }
        return username;
    }

    //通过参数生成token
    public String generateToke(Map<String, Object> claims){
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationTime())
                .signWith(SignatureAlgorithm.HS256,Constant.SECRET)
                .compact();
    }

    //通过username生成token
    public String generateToke(String username){
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationTime())
                .signWith(SignatureAlgorithm.HS256,Constant.SECRET)
                .compact();
    }

    //通过安全用户生成token
    public String generateToke(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToke(claims);
    }

    //生成过期时间
    public Date generateExpirationTime() {
        return new Date(System.currentTimeMillis()+ Constant.EXPIRATION_TIME);
    }

    //从token中获取创建时间
    public Date getCreatedDateFromToken(String token){
        Date created;
        try{
            Claims claims = getClaimsFromToken(token);
            created = (Date) claims.get(CLAIM_KEY_CREATED);
        }catch (Exception e){
            created = null;
            e.printStackTrace();
        }
        return created;
    }

    //从token中获取过期时间
    public Date getExpirationDateFromToken(String token){
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        }catch (Exception e){
            e.printStackTrace();
            expiration = null;
        }
        return expiration;
    }

    //token是否过期     判断过期时间是否在当前时间前
    private Boolean isTokenExpired(String token){
        Date date = getExpirationDateFromToken(token);
        return date.before(new Date());
    }

    //刷新token
    public String refreshToken(String token){
        String refreshToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED,new Date());
            refreshToken = generateToke(claims);
        }catch (Exception e){
            e.printStackTrace();
            refreshToken = null;
        }
        return refreshToken;
    }

    //没有过期才能刷新
    public Boolean canTokenBeRefreshed(String token){
        return !isTokenExpired(token);
    }

    //校验token   username一致，没有过期
    public Boolean validateToken(String token,UserDetails userDetails){
        SysUser user = (SysUser) userDetails;
        String username = getUsernameFromToken(token);
        return user.getUsername().equals(username)&&canTokenBeRefreshed(token);
    }


//    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
//        return (lastPasswordReset != null && created.before(lastPasswordReset));
//    }
}

























