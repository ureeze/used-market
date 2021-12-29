package com.example.usedmarket.web.security.jwt;

import com.example.usedmarket.web.config.AppProperties;
import com.example.usedmarket.web.domain.user.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private final AppProperties appProperties;


    public String create(UserEntity userEntity) {
        Date expiryDate = new Date(new Date().getTime() + appProperties.getAuth().getTokenExpirationMsec());
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .setSubject(userEntity.getEmail())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    public String validateAndGetEmail(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
