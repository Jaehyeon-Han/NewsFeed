package org.springfeed.newsfeed.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final long TOKEN_TIME = 30 * 60 * 1000L;
    private final SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 초기화
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(Long userId) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date)
                .signWith(key, algorithm)
                .compact();
    }

    //토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {       // 토큰이 만료된 경우
            log.error("만료된 토큰입니다.", e);
        } catch (SignatureException e) {        // 토큰 서명이 잘못된 경우
            log.error("서명이 유효하지 않은 토큰입니다.", e);
        } catch (MalformedJwtException | UnsupportedJwtException | SecurityException e) {     // 잘못된 형식의 JWT가 전달된 경우
            log.error("잘못된 형식의 토큰입니다.", e);
        } catch (IllegalArgumentException e) {  // 토큰이 없는 경우
            log.error("잘못된 토큰 입니다.", e);
        }
        return false;
    }

    // jwt에 있는 userId값 반환
    public Long getUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String subject = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody().getSubject();
        return Long.valueOf(subject);
    }
}
