package com.sportfy.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sportfy.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public String createToken(User user) {

        return JWT.create()
                .withIssuer("user")
                .withSubject(user.getUsername())
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole())
                .withExpiresAt(LocalDateTime.now().plusMinutes(140).toInstant(ZoneOffset.of("-03:00")))
                .sign(Algorithm.HMAC256("secreta"));
    }
    public String getSubject(String token){
        return JWT.require(Algorithm.HMAC256("secreta"))
                .withIssuer("user")
                .build().verify(token).getSubject();
    }
}
