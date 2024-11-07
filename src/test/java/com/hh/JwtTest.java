package com.hh;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "张三");

        String token = JWT.create()
                .withClaim("user", claims) //载荷信息
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) //过期时间
                .sign(Algorithm.HMAC256("xiaoXiao"));  //指定加密算法

        System.out.println(token);

    }

    @Test
    public void testParse() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3MjcwOTczOTZ9.dZ-M-R4-M4qXeN4Bfv1MI_syacluDTby6DSGU9GSXsc";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("xiaoXiao")).build(); //解析器

        DecodedJWT verify = jwtVerifier.verify(token);//将token给解析器
        Map<String, Claim> claims = verify.getClaims(); //获取载荷信息存储到Map里面
        System.out.println(claims.get("user"));
    }
}
